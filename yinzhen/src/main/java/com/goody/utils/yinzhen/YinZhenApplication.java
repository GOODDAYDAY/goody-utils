package com.goody.utils.yinzhen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * ApiGatewayApplication
 *
 * @author yuzehui
 * @version 1.0, 2022/12/2 15:28
 * @since 1.0.0
 */
@ComponentScan(basePackages = {"com.goody.utils.yinzhen"})
@SpringBootApplication
public class YinZhenApplication {
    public static void main(String[] args) {
        SpringApplication.run(YinZhenApplication.class, args);
    }
}
