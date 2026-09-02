package com.huysg136.cirquo_server.auth.service;

import com.huysg136.cirquo_server.auth.dto.request.RegisterRequest;
import com.huysg136.cirquo_server.user.dto.response.UserResponse;

public interface AuthService {
    UserResponse register(RegisterRequest request);
}
