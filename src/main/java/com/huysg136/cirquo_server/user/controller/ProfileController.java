package com.huysg136.cirquo_server.user.controller;

import com.huysg136.cirquo_server.common.ApiResponse;
import com.huysg136.cirquo_server.common.BaseController;
import com.huysg136.cirquo_server.user.dto.request.UpdateUserRequest;
import com.huysg136.cirquo_server.user.dto.response.UserResponse;
import com.huysg136.cirquo_server.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(
        name = "Profile",
        description = "Manage the authenticated user's profile"
)
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
public class ProfileController extends BaseController {

    private final UserService userService;

    @Operation(
            summary = "Get current profile",
            description = "Returns the profile of the authenticated user."
    )
    @GetMapping
    public ResponseEntity<ApiResponse<UserResponse>> getProfile(
            Authentication authentication
    ) {
        return success(
                HttpStatus.OK,
                "Profile retrieved successfully!",
                userService.getUserById(getCurrentUserId(authentication))
        );
    }

    @Operation(
            summary = "Update current profile",
            description = "Updates the profile of the authenticated user."
    )
    @PutMapping
    public ResponseEntity<ApiResponse<UserResponse>> updateProfile(
            Authentication authentication,
            @Valid @RequestBody UpdateUserRequest request
    ) {
        return success(
                HttpStatus.OK,
                "Profile updated successfully!",
                userService.updateUser(
                        getCurrentUserId(authentication),
                        request
                )
        );
    }

    private UUID getCurrentUserId(Authentication authentication) {
        return UUID.fromString(authentication.getName());
    }
}
