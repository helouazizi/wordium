package com.wordium_backend.test_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
// @EnableDiscoveryClient
public class Hello {

    @GetMapping("/")
    public String home() {
        return "Hello World from test home";
    }

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }
}
