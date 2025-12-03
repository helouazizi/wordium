package com.wordium_backend.test_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication

public class TestServiceApplication {

	@RequestMapping("/")
	public String home() {
		return "Hello World from test";
	}

	public static void main(String[] args) {
		SpringApplication.run(TestServiceApplication.class, args);
	}

}


