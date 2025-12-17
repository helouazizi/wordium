package com.wordium_backend.api_gatway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class LoggingFilter implements GlobalFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, org.springframework.cloud.gateway.filter.GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        log.info("🟢 Incoming request URL: {}", request.getURI());
        log.info("Method: {}", request.getMethod());
        request.getHeaders().forEach((name, values) -> log.info("Header: {} = {}", name, values));

        return chain.filter(exchange)
                .doOnError(throwable -> log.error("❌ Error forwarding request: {}", throwable.getMessage(), throwable));
    }

    @Override
    public int getOrder() {
        return -100; // run early
    }
}
