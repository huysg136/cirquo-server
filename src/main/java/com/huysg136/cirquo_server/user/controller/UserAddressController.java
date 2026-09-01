package com.huysg136.cirquo_server.user.controller;

import com.huysg136.cirquo_server.common.ApiResponse;
import com.huysg136.cirquo_server.common.BaseController;
import com.huysg136.cirquo_server.user.dto.request.UserAddressRequest;
import com.huysg136.cirquo_server.user.dto.response.UserAddressResponse;
import com.huysg136.cirquo_server.user.service.UserAddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users/{userId}/addresses")
@RequiredArgsConstructor
public class UserAddressController extends BaseController {
    private final UserAddressService userAddressService;

    @PostMapping
    public ResponseEntity<ApiResponse<UserAddressResponse>> createAddress (
            @PathVariable UUID userId,
            @Valid @RequestBody UserAddressRequest userAddressRequest
    ) {
        UserAddressResponse userAddressResponse = userAddressService.createAddress(userId, userAddressRequest);

        return success(
                HttpStatus.CREATED,
                "User address created successfully!",
                userAddressResponse
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserAddressResponse>>> getAllByUserId (
            @PathVariable UUID userId
    ){
        return success(
                HttpStatus.OK,
                "User addresses retrieved successfully!",
                userAddressService.getAllByUserId(userId)
        );
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<ApiResponse<UserAddressResponse>> updateAddress (
            @PathVariable UUID userId,
            @PathVariable UUID addressId,
            @Valid @RequestBody UserAddressRequest userAddressRequest
    ){
        UserAddressResponse userAddressResponse = userAddressService.updateAddress(userId, addressId, userAddressRequest);

        return  success(
                HttpStatus.OK,
                "User address updated successfully!",
                userAddressResponse
        );
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<ApiResponse<Void>> deleteAddress(
            @PathVariable UUID userId,
            @PathVariable UUID addressId
    ) {
        userAddressService.deleteAddress(userId ,addressId);

        return success(
                HttpStatus.OK,
                "User address deleted successfully!",
                null
        );
    }
}
