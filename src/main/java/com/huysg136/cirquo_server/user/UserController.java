package com.huysg136.cirquo_server.user;

import com.huysg136.cirquo_server.common.ApiResponse;
import com.huysg136.cirquo_server.common.BaseController;
import com.huysg136.cirquo_server.user.dto.request.CreateUserRequest;
import com.huysg136.cirquo_server.user.dto.request.UpdateUserRequest;
import com.huysg136.cirquo_server.user.dto.response.ListUserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/v1/users")
@RequiredArgsConstructor
public class UserController extends BaseController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createUser(
            @Valid @RequestBody CreateUserRequest request
    ) {
        userService.create(request);

        return success(
                HttpStatus.CREATED,
                "User created successfully",
                null
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ListUserResponse>>> getAllUsers() {
        return success(
                HttpStatus.OK,
                "Users retrieved successfully",
                userService.read()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> updateUser(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateUserRequest request
    ) {
        userService.update(id, request);

        return success(
                HttpStatus.OK,
                "User updated successfully",
                null
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(
            @PathVariable UUID id
    ) {
        userService.delete(id);

        return success(
                HttpStatus.OK,
                "User deleted successfully",
                null
        );
    }
}
