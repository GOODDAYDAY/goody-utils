package com.goody.utils.jinjunmei.feign;

import feign.Client;
import feign.Request;
import feign.Response;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerProperties;
import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.cloud.openfeign.loadbalancer.FeignBlockingLoadBalancerClient;
import org.springframework.cloud.openfeign.loadbalancer.OnRetryNotEnabledCondition;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;

@Component
public class LoadBalancerConfig {

    @Primary
    @Bean
    @ConditionalOnMissingBean
    @Conditional({OnRetryNotEnabledCondition.class})
    public Client feignClient(LoadBalancerClient loadBalancerClient, LoadBalancerProperties properties, LoadBalancerClientFactory loadBalancerClientFactory) {
        Client.Default delegate = new Client.Default((SSLSocketFactory) null, (HostnameVerifier) null) {
            @Override
            public Response execute(Request request, Request.Options options) throws IOException {
                System.out.println(request.url());
                final long start = System.currentTimeMillis();
                Response response = super.execute(request, options);
                System.out.println(System.currentTimeMillis() - start);
                return response;
            }
        };
        return new FeignBlockingLoadBalancerClient(delegate, loadBalancerClient, properties, loadBalancerClientFactory);
    }

    @Bean
    @ConditionalOnBean({DiscoveryClient.class})
    @ConditionalOnMissingBean
    @ConditionalOnProperty(
        value = {"spring.cloud.loadbalancer.configurations"},
        havingValue = "default",
        matchIfMissing = true
    )
    public ServiceInstanceListSupplier discoveryClientServiceInstanceListSupplier(ConfigurableApplicationContext context) {
        return ServiceInstanceListSupplier.builder().withBlockingDiscoveryClient().withCaching().build(context);
    }

    @Bean
    @Primary
    public ReactorLoadBalancer<ServiceInstance> roundRobinLoadBalancer(Environment environment, ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider) {
        return new RoundRobinLoadBalancer(serviceInstanceListSupplierProvider, "jinjunmei-server");
    }
}
