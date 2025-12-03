package com.wordium_backend.test1_service.service;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ServiceAConsumer {

    private final DiscoveryClient discoveryClient;
    private final RestTemplate restTemplate = new RestTemplate();

    public ServiceAConsumer(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    public String callServiceA() {
        // Get all instances of service-a
        var instances = discoveryClient.getInstances("test-service");
        if (instances.isEmpty())
            return "Service A not available";

        // Take first instance
        ServiceInstance instance = instances.get(0);
        String url = instance.getUri() + "/";

        // Call Service A
        return restTemplate.getForObject(url, String.class);
    }
}
