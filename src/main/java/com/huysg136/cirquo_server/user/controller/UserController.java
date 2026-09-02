package com.huysg136.cirquo_server.user.controller;

import com.huysg136.cirquo_server.common.ApiResponse;
import com.huysg136.cirquo_server.common.BaseController;
import com.huysg136.cirquo_server.user.service.UserService;
import com.huysg136.cirquo_server.user.dto.request.ChangeUserStatusRequest;
import com.huysg136.cirquo_server.user.dto.request.UpdateUserRequest;
import com.huysg136.cirquo_server.user.dto.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(
        name = "Users",
        description = "Manage user profiles and account statuses"
)
@RestController
@RequestMapping(value = "/api/v1/users")
@RequiredArgsConstructor
public class UserController extends BaseController {
    private final UserService userService;

    @Operation(
            summary = "Get all users",
            description = "Returns a list of all user accounts."
    )
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        return success(
                HttpStatus.OK,
                "Users retrieved successfully!",
                userService.getAllUsers()
        );
    }

    @Operation(
            summary = "Get user by ID",
            description = "Returns the profile and account status of the specified user."
    )
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(
            @PathVariable UUID userId
    ) {
        return success(
                HttpStatus.OK,
                "User retrieved successfully!",
                userService.getUserById(userId)
        );
    }

    @Operation(
            summary = "Update user profile",
            description = "Updates the email, full name, and phone number of the specified user."
    )
    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(
            @PathVariable UUID userId,
            @Valid @RequestBody UpdateUserRequest request
    ) {

        UserResponse userResponse = userService.updateUser(userId, request);

        return success(
                HttpStatus.OK,
                "User updated successfully!",
                userResponse
        );
    }

    @Operation(
            summary = "Change user status",
            description = "Changes the account status to ACTIVE, INACTIVE, or SUSPENDED without deleting the user."
    )
    @PatchMapping("/{userId}/status")
    public ResponseEntity<ApiResponse<Void>> changeStatus(
            @PathVariable UUID userId,
            @Valid @RequestBody ChangeUserStatusRequest request
    ) {
        userService.changeStatus(userId, request.status());

        return success(
                HttpStatus.OK,
                "User status changed successfully!",
                null
        );
    }
}
