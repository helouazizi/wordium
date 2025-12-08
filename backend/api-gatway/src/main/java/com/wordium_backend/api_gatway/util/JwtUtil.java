package com.wordium_backend.api_gatway.util;

import java.security.Key;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

@Component
public class JwtUtil {

    private final Key userTokenKey;
    private final String serviceTokenKey;


    public String getServiceTokenKey() {
        return serviceTokenKey;
    }

    public JwtUtil(
            @Value("${JWT_SECRET}") String userSecret,
            @Value("${INTERNAL_SERVICE_SECRET}") String serviceSecret
    ) {
        this.userTokenKey = Keys.hmacShaKeyFor(userSecret.getBytes());
        this.serviceTokenKey = serviceSecret;
    }

    // Validate normal user token
    public Claims validateUserToken(String token) throws SignatureException {
        return Jwts.parserBuilder()
                .setSigningKey(userTokenKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Validate service-to-service token
    // public Claims validateServiceToken(String token) throws SignatureException {
    //     return Jwts.parserBuilder()
    //             .setSigningKey(serviceTokenKey)
    //             .build()
    //             .parseClaimsJws(token)
    //             .getBody();
    // }
}
