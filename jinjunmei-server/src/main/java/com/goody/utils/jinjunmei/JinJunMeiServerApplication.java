package com.goody.utils.jinjunmei;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * JinJunMeiServerApplication
 *
 * @author Goody
 * @version 1.0, 2022/12/2 15:28
 * @since 1.0.0
 */
@ComponentScan(basePackages = {"com.goody.utils.jinjunmei"})
@EnableScheduling
@EnableDiscoveryClient
@SpringBootApplication
public class JinJunMeiServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(JinJunMeiServerApplication.class, args);
    }
}
