package com.goody.utils.jinjunmei.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO
 *
 * @author Goody
 * @version 1.0, 2023/5/12
 * @since 1.0.0
 */
@RestController
@RequestMapping("/goody")
@RequiredArgsConstructor
public class GoodyController {

    @PostMapping("/1")
    public String query1(@RequestBody String form) {
        System.out.println("12123123");
        return form;
    }

    @GetMapping("/1")
    public String query(@RequestBody String form) {
        System.out.println("query");
        return form;
    }

    @PostMapping("/2")
    public String query22(@RequestBody String form) {
//        try {
//            TimeUnit.SECONDS.sleep(10);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
        System.out.println("query22");
        return form;
//        throw new IllegalArgumentException("query22");
    }

    @GetMapping("/2")
    public String query2(@RequestBody String form) {
        System.out.println("query2");
        return form;
    }
}
