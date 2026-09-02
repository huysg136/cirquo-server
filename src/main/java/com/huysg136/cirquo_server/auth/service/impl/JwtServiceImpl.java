package com.huysg136.cirquo_server.auth.service.impl;

import com.huysg136.cirquo_server.auth.service.JwtService;
import com.huysg136.cirquo_server.user.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access-token-expiration}")
    private Duration accessTokenExpiration;

    @Value("${jwt.refresh-token-expiration}")
    private Duration refreshTokenExpiration;

    @Override
    public boolean isRefreshToken(String token) {
        try {
            String tokenType = extractClaims(token)
                    .get("tokenType", String.class);

            return "refresh".equals(tokenType);
        } catch (JwtException | IllegalArgumentException exception) {
            return false;
        }
    }

    @Override
    public boolean isAccessToken(String token) {
        try {
            String tokenType = extractClaims(token)
                    .get("tokenType", String.class);

            return "access".equals(tokenType);
        } catch (JwtException | IllegalArgumentException exception) {
            return false;
        }
    }

    @Override
    public String generateAccessToken(User user) {
        return createToken(user, accessTokenExpiration, "access");
    }

    @Override
    public String generateRefreshToken(User user) {
        return createToken(user, refreshTokenExpiration, "refresh");
    }

    @Override
    public String extractEmail(String token) {
        return extractClaims(token).getSubject();
    }

    @Override
    public UUID extractUserId(String token) {
        String userId = extractClaims(token).get("userId", String.class);
        return UUID.fromString(userId);
    }

    @Override
    public boolean isTokenValid(String token) {
        try {
            extractClaims(token);
            return true;
        } catch (JwtException    | IllegalArgumentException exception) {
            return false;
        }
    }

    private String createToken(
            User user,
            Duration expiration,
            String tokenType
    ) {
        Instant now = Instant.now();

        return Jwts.builder()
                .subject(user.getEmail())
                .claim("userId", user.getId().toString())
                .claim("role", user.getRole().getName().name())
                .claim("tokenType", tokenType)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(expiration)))
                .signWith(getSigningKey())
                .compact();
    }

    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
