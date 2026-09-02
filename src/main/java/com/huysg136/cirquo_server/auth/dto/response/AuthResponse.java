package com.huysg136.cirquo_server.auth.dto.response;

import com.huysg136.cirquo_server.user.dto.response.UserResponse;

public record AuthResponse(
        String accessToken,
        String refreshToken,
        String tokenType,
        UserResponse user
) {
}
