package com.wordium_backend.api_gatway.util;

import java.security.Key;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

@Component
public class JwtUtil {

    // Must be at least 256 bits (32 chars) for HS256
    private final String SECRET = "my-very-strong-secret-key-that-is-at-least-64-chars-long!!!";
    private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

    public Claims validateToken(String token) throws SignatureException {
        return Jwts.parserBuilder()   
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
