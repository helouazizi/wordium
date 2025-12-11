package com.wordium.auth.util;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wordium.auth.dto.ApiError;
import com.wordium.auth.exceptions.ExternalServiceException;

import reactor.core.publisher.Mono;

@Component
public class ServiceCaller {

    private final ObjectMapper objectMapper;

    public ServiceCaller(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public <T> T callService(Mono<T> mono) {
        try {
            return mono.block();
        } catch (WebClientResponseException e) {
            try {
                ApiError error = objectMapper.readValue(e.getResponseBodyAsString(), ApiError.class);
                System.err.println(error);
                throw new ExternalServiceException(
                        error.status(),
                        error.error(),
                        error.message(),
                        error.fieldErrors());
            } catch (JsonProcessingException ex) {
                throw new ExternalServiceException(
                        e.getStatusCode().value(),
                        "External Service Error",
                        e.getResponseBodyAsString(),
                        null);
            }
        }
    }
}
