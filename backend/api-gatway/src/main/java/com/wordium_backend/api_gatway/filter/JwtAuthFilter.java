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
        System.out.println("[Gateway Filter] Incoming path: " + path);

        // Allow auth endpoints without token
        if (path.startsWith("/api/v1/auth") || path.contains("/v3/api-docs")) {
            System.out.println("[Gateway Filter] Auth route detected, skipping auth check");
            return chain.filter(exchange);
        }

        // validate internal connections
        if (isInternalUsersRoute(path)) {
            String internalToken = exchange.getRequest().getHeaders().getFirst("Internal-Service-Token");

            System.out.println("[Gateway Filter] Internal token received: " + internalToken);
            if (internalToken == null || !internalToken.equals(jwtUtil.getServiceTokenKey())) {
                System.out.println("[Gateway Filter] Invalid internal token");
                return unauthorized(exchange);
            }

            System.out.println("[Gateway Filter] Internal token valid");
            return chain.filter(exchange);
        }

        var req = exchange.getRequest();
        if (!req.getHeaders().containsKey("Authorization")) {
            System.out.println("[Gateway Filter] Missing Authorization header, returning 401");
            return unauthorized(exchange);
        }

        String authHeader = req.getHeaders().getFirst("Authorization");
        System.out.println("[Gateway Filter] Authorization header: " + authHeader);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("[Gateway Filter] Invalid Authorization header, returning 401");
            return unauthorized(exchange);
        }

        String token = authHeader.substring(7);
        try {
            jwtUtil.validateUserToken(token);
            System.out.println("[Gateway Filter] User token validated successfully!");
        } catch (Exception e) {
            System.out.println("[Gateway Filter] Invalid user token: " + e.getMessage());
            return unauthorized(exchange);
        }

        return chain.filter(exchange);
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        System.out.println("[Gateway Filter] Returning 401 Unauthorized");
        return exchange.getResponse().setComplete();
    }

    @Override
    public int getOrder() {
        return -1; // ensures filter runs early
    }

    private boolean isInternalUsersRoute(String path) {
        return path.startsWith("/api/v1/users")
                || path.startsWith("/api/v1/users/by-email");
    }
}
