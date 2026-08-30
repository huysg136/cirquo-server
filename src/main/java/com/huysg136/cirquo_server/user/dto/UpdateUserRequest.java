package com.huysg136.cirquo_server.user.dto;

public record UpdateUserRequest(
        String email,
        String fullName,
        String phone
) {
}
