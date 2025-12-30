package com.wordium.users.config;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.Client;
import feign.httpclient.ApacheHttpClient;

@Configuration
public class FeignClientConfig {

    @Bean
    public Client feignClient() {
        CloseableHttpClient httpClient = HttpClients.custom().build();
        return new ApacheHttpClient(httpClient);
    }
}
