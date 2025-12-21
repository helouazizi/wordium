package com.wordium.wsgateway.util;

import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wordium.wsgateway.common.exceptions.ExternalServiceProblemException;

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
            throw mapProblemDetail(e);
        }
    }

    private RuntimeException mapProblemDetail(WebClientResponseException e) {
        try {
            ProblemDetail problemDetail = objectMapper.readValue(e.getResponseBodyAsString(), ProblemDetail.class);

            return new ExternalServiceProblemException(problemDetail);

        } catch (Exception ex) {
            ProblemDetail fallback = ProblemDetail.forStatus(e.getStatusCode());
            fallback.setTitle("External Service Error");
            fallback.setDetail(e.getResponseBodyAsString());
            return new ExternalServiceProblemException(fallback);
        }
    }
}
