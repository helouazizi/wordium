package com.wordium_backend.test1_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wordium_backend.test1_service.service.ServiceAConsumer;

@SpringBootApplication
@RestController
@EnableDiscoveryClient
public class Test1ServiceApplication {

	private final ServiceAConsumer serviceAConsumer;

	public Test1ServiceApplication(ServiceAConsumer serviceAConsumer) {
		this.serviceAConsumer = serviceAConsumer;
	}

	@RequestMapping("/call-test")
	public String test1() {
		return serviceAConsumer.callServiceA();
	}

	@RequestMapping("/hello")
	public String hello() {
		return "heloo from consummer";
	}

	public static void main(String[] args) {
		SpringApplication.run(Test1ServiceApplication.class, args);
	}

}
