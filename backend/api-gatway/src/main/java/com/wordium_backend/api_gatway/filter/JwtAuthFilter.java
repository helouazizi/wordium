package com.wordium_backend.api_gatway.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.wordium_backend.api_gatway.util.JwtUtil;

import reactor.core.publisher.Mono;

@Component
public class JwtAuthFilter implements GlobalFilter, Ordered {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String path = exchange.getRequest().getURI().getPath();

        // Allow auth endpoints without token
        if (path.startsWith("/api/v1/auth") || path.startsWith("/api/v1/wsgateway/ws") || path.contains("/v3/api-docs")) {
            return chain.filter(exchange);
        }

        // validate internal connections
        if (path.contains("internal")) {
            String internalToken = exchange.getRequest().getHeaders().getFirst("Internal-Service-Token");

            if (internalToken == null || !internalToken.equals(jwtUtil.getServiceTokenKey())) {
                return unauthorized(exchange);
            }

            return chain.filter(exchange);
        }

        var req = exchange.getRequest();
        if (!req.getHeaders().containsKey("Authorization")) {
            return unauthorized(exchange);
        }

        String authHeader = req.getHeaders().getFirst("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return unauthorized(exchange);
        }

        String token = authHeader.substring(7);
        try {
            jwtUtil.validateUserToken(token);
        } catch (Exception e) {
            return unauthorized(exchange);
        }

        JwtUtil.UserInfo userinfo = jwtUtil.extractUserInfo(token);
        var changedRequest = exchange.mutate()
                .request(builder -> builder
                .header("User-Id", userinfo.userId())
                .header("User-Role", userinfo.role()))
                .build();

        return chain.filter(changedRequest);
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

    @Override
    public int getOrder() {
        return -1;
    }

}
