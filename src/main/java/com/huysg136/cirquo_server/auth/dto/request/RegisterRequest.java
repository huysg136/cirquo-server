package com.huysg136.cirquo_server.auth.dto.request;

import com.huysg136.cirquo_server.annotation.PhoneNumber;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "Email is required!")
        @Email(message = "Email is invalid!")
        @Size(max = 255, message = "Email must not exceed 255 characters!")
        String email,

        @NotBlank(message = "Password is required!")
        @Size(min = 8, max = 72, message = "Password must be 8 to 72 characters!")
        String password,

        @NotBlank(message = "Full name is required!")
        @Size(max = 100, message = "Full name must not exceed 100 characters!")
        String fullName,

        @PhoneNumber
        String phone
) {
}
