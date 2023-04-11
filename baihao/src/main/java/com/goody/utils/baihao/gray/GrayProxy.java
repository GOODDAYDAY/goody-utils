package com.goody.utils.baihao.gray;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.DynamicMethodMatcherPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * TODO
 *
 * @author Goody
 * @version 1.0, 2023/4/6
 * @since 1.0.0
 */
@Component
@Slf4j
public class GrayProxy {

    @Bean
    public DefaultPointcutAdvisor grayAdvice() {
        return new DefaultPointcutAdvisor(new GrayProxyPoint(), new GrayProxyAspect());
    }

    public static class GrayProxyAspect implements MethodInterceptor {

        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            System.out.println("命中");
            return invocation.proceed();
        }

    }

    public static class GrayProxyPoint extends DynamicMethodMatcherPointcut {
        @Override
        public boolean matches(Method method, Class<?> targetClass, Object... args) {
            return targetClass.getPackage().getName().startsWith("com.goody");
        }


        @Override
        public ClassFilter getClassFilter() {
            return clazz -> {
                System.out.println(clazz.getName());
                return clazz.getPackage().getName().startsWith("com.goody");
            };
        }
    }
}
