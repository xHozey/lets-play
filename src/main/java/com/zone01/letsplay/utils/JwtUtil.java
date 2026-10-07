package com.zone01.letsplay.utils;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.zone01.letsplay.model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
    private final Key secretKey;
    private final long EXPIRATION = 10 * 1000 * 60;

    public JwtUtil(@Value("${spring.jwt.secret}") String secret) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAuthenticationToken(User user) {
        return Jwts.builder().subject(user.getId()).claim("name", user.getName())
                .claim("role", "ROLE_" + user.getRole()).issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION)).signWith(secretKey).compact();
    }

    public boolean isTokenExpired(String token) {
        Date expire = Jwts.parser()
                .verifyWith((SecretKey) secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();
        return expire.before(new Date());
    }

    public User extractUser(String token) {
        Claims claims = Jwts.parser()
                .verifyWith((SecretKey) secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        User user = new User();
        user.setId(claims.getSubject());
        user.setName(claims.get("name", String.class));

        String role = claims.get("role", String.class);
        if (role != null && role.startsWith("ROLE_")) {
            user.setRole(role.substring(5));
        }

        return user;
    }

}