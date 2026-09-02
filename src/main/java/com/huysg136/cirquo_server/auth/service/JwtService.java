package com.huysg136.cirquo_server.auth.service;

import com.huysg136.cirquo_server.user.entity.User;

import java.util.UUID;

public interface JwtService {
    boolean isRefreshToken(String token);

    boolean isAccessToken(String token);

    String generateAccessToken(User user);

    String generateRefreshToken(User user);

    String extractEmail(String token);

    UUID extractUserId(String token);

    boolean isTokenValid(String token);
}
