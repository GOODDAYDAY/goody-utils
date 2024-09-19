package com.goody.utils.jinjunmei;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * JinJunMeiNoNacosServerApplication
 *
 * @author Goody
 * @version 1.0, 2022/12/2 15:28
 * @since 1.0.0
 */
@ComponentScan(basePackages = {"com.goody.utils.jinjunmei"})
@SpringBootApplication
public class JinJunMeiNoNacosServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(JinJunMeiNoNacosServerApplication.class, args);
    }
}
