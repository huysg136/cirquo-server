package com.huysg136.cirquo_server.auth.controller;

import com.huysg136.cirquo_server.auth.dto.request.ChangePasswordRequest;
import com.huysg136.cirquo_server.auth.dto.request.LoginRequest;
import com.huysg136.cirquo_server.auth.dto.request.RefreshTokenRequest;
import com.huysg136.cirquo_server.auth.dto.request.RegisterRequest;
import com.huysg136.cirquo_server.auth.dto.response.AuthResponse;
import com.huysg136.cirquo_server.auth.service.AuthService;
import com.huysg136.cirquo_server.common.ApiResponse;
import com.huysg136.cirquo_server.common.BaseController;
import com.huysg136.cirquo_server.user.dto.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(
        name = "Authentication",
        description = "Register, login, and token management"
)
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController extends BaseController {
    private final AuthService authService;

    @Operation(
            summary = "Register a new customer account",
            description = "Creates a customer account with the default CUSTOMER role."
    )
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        UserResponse userResponse = authService.register(request);

        return success(
                HttpStatus.CREATED,
                "User registered successfully!",
                userResponse
        );
    }

    @Operation(
            summary = "Login",
            description = "Authenticates a user and returns access and refresh tokens."
    )
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request
    ) {
        return success(
                HttpStatus.OK,
                "Login successful!",
                authService.login(request)
        );
    }

    @Operation(
            summary = "Refresh access token",
            description = "Uses a valid refresh token to issue a new access token."
    )
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(
            @Valid @RequestBody RefreshTokenRequest request
    ) {
        return success(
                HttpStatus.OK,
                "Access token refreshed successfully!",
                authService.refreshToken(request)
        );
    }

    @Operation(
            summary = "Change password",
            description = "Changes the password of the specified user after verifying the current password."
    )
    @SecurityRequirement(name = "bearerAuth")
    @PatchMapping("/users/{userId}/password")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @PathVariable UUID userId,
            @Valid @RequestBody ChangePasswordRequest request
    ) {
        authService.changePassword(userId, request);

        return success(
                HttpStatus.OK,
                "Password changed successfully!",
                null
        );
    }
}
