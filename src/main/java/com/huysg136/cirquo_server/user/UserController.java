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
    public ApiResponse<String> createUser(@Valid @RequestBody CreateUserRequest createUserRequest){
        userService.create(createUserRequest);
        return createSuccessResponse("User created successfully!");
    }

    @GetMapping
    public ApiResponse<List<ListUserResponse>> getAllUsers(){
        List<ListUserResponse> users = userService.read();
        return createSuccessResponse(users);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable("id") UUID id, @Valid @RequestBody UpdateUserRequest updateUserRequest){
        userService.update(id, updateUserRequest);
        return ResponseEntity.status(HttpStatus.OK).body("User updated successfully!");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") UUID id){
        userService.delete(id);
        return  ResponseEntity.status(HttpStatus.OK).body("User deleted successfully!");
    }
}
