package com.huysg136.cirquo_server.user.dto;

public record CreateUserRequest (
        String email,
        String password,
        String fullName,
        String phone
) {
}
