package com.goody.utils.houkui;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * test application
 *
 * @author Goody
 * @version 1.0, 2023/1/11 16:40
 * @since 1.0.0
 */
@ComponentScan("com.goody.utils.houkui")
@MapperScan("com.goody.utils.houkui.datahandler")
@SpringBootApplication
public class TestApplication {
    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }
}
