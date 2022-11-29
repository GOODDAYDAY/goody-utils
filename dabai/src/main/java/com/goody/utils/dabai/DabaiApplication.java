package com.goody.utils.dabai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Dabai Application
 *
 * @author Goody
 * @version 1.0, 2022/11/29 15:11
 * @since 1.1.0
 */
@SpringBootApplication
@ComponentScan("com.goody")
public class DabaiApplication {

    public static void main(String[] args) {
        SpringApplication.run(DabaiApplication.class, args);
    }
}
