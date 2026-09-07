package com.huysg136.cirquo_server.user.controller;

import com.huysg136.cirquo_server.common.ApiResponse;
import com.huysg136.cirquo_server.common.BaseController;
import com.huysg136.cirquo_server.common.PageResponse;
import com.huysg136.cirquo_server.user.dto.request.ChangeUserRoleRequest;
import com.huysg136.cirquo_server.user.dto.request.ChangeUserStatusRequest;
import com.huysg136.cirquo_server.user.dto.request.UpdateUserRequest;
import com.huysg136.cirquo_server.user.dto.response.UserResponse;
import com.huysg136.cirquo_server.user.enums.RoleName;
import com.huysg136.cirquo_server.user.enums.UserStatus;
import com.huysg136.cirquo_server.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(
        name = "Admin Users",
        description = "Manage user accounts"
)
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
@RestController
@RequestMapping("/api/v1/admin/users")
@RequiredArgsConstructor
public class AdminUserController extends BaseController {

    private final UserService userService;

    @Operation(
            summary = "Get users for administration",
            description = "Returns paginated user accounts with optional filters."
    )
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<UserResponse>>> getUsers(
            @RequestParam(required = false) UserStatus status,
            @RequestParam(required = false) RoleName roleName,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = "Page must not be negative!")
            int page,
            @RequestParam(defaultValue = "20")
            @Min(value = 1, message = "Size must be greater than 0!")
            @Max(value = 100, message = "Size must not exceed 100!")
            int size
    ) {
        return success(
                HttpStatus.OK,
                "Users retrieved successfully!",
                userService.getUsersForAdmin(
                        status,
                        roleName,
                        keyword,
                        page,
                        size
                )
        );
    }

    @Operation(
            summary = "Get a user by ID",
            description = "Returns the specified user account for administration."
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
            summary = "Update a user",
            description = "Updates the specified user's profile information."
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(
            @PathVariable UUID userId,
            @Valid @RequestBody UpdateUserRequest request
    ) {
        return success(
                HttpStatus.OK,
                "User updated successfully!",
                userService.updateUser(userId, request)
        );
    }

    @Operation(
            summary = "Change user status",
            description = "Changes the account status without deleting the user."
    )
    @PreAuthorize("hasRole('ADMIN')")
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

    @Operation(
            summary = "Change user role",
            description = "Changes the role of the specified user."
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{userId}/role")
    public ResponseEntity<ApiResponse<UserResponse>> changeRole(
            @PathVariable UUID userId,
            @Valid @RequestBody ChangeUserRoleRequest request
    ) {
        return success(
                HttpStatus.OK,
                "User role changed successfully!",
                userService.changeRole(userId, request.roleName())
        );
    }
}
