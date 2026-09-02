package com.huysg136.cirquo_server.auth.service;

import com.huysg136.cirquo_server.auth.dto.request.ChangePasswordRequest;
import com.huysg136.cirquo_server.auth.dto.request.LoginRequest;
import com.huysg136.cirquo_server.auth.dto.request.RefreshTokenRequest;
import com.huysg136.cirquo_server.auth.dto.request.RegisterRequest;
import com.huysg136.cirquo_server.auth.dto.response.AuthResponse;
import com.huysg136.cirquo_server.user.dto.response.UserResponse;

import java.util.UUID;

public interface AuthService {
    UserResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    AuthResponse refreshToken(RefreshTokenRequest request);

    void changePassword(UUID userId, ChangePasswordRequest request);
}
