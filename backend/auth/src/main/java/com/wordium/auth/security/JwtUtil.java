package com.wordium.auth.security;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    private final String SECRET;
    private final long EXPIRATION = 24 * 60 * 60 * 1000; // 1 day
    private final Key key;

    public JwtUtil(@Value("${JWT_SECRET}") String secret) {
        this.SECRET = secret;
        this.key = Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    public String generateToken(Long id, String role) {
        return Jwts.builder()
                .setSubject(id.toString())
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(key)
                .compact();
    }

    // public Long extractUserId(String token) {
    // Claims claims = Jwts.parserBuilder()
    // .setSigningKey(key) // same key used to sign
    // .build()
    // .parseClaimsJws(token)
    // .getBody();

    // return Long.valueOf(claims.getSubject());
    // }
}
