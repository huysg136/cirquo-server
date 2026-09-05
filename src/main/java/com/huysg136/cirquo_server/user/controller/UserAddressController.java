package com.huysg136.cirquo_server.user.controller;

import com.huysg136.cirquo_server.common.ApiResponse;
import com.huysg136.cirquo_server.common.BaseController;
import com.huysg136.cirquo_server.user.dto.request.UserAddressRequest;
import com.huysg136.cirquo_server.user.dto.response.UserAddressResponse;
import com.huysg136.cirquo_server.user.service.UserAddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(
        name = "User Addresses",
        description = "Manage delivery addresses for a user"
)
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("""
        #userId.toString() == authentication.name
        or hasRole('ADMIN')
        """)
@RestController
@RequestMapping("/api/v1/users/{userId}/addresses")
@RequiredArgsConstructor
public class UserAddressController extends BaseController {
    private final UserAddressService userAddressService;

    @Operation(
            summary = "Create a user address",
            description = "Creates a delivery address for the specified user. A default address replaces the user's current default address."
    )
    @PostMapping
    public ResponseEntity<ApiResponse<UserAddressResponse>> createAddress (
            @PathVariable UUID userId,
            @Valid @RequestBody UserAddressRequest userAddressRequest
    ) {

        return success(
                HttpStatus.CREATED,
                "User address created successfully!",
                userAddressService.createAddress(userId, userAddressRequest)
        );
    }

    @Operation(
            summary = "Get user addresses",
            description = "Returns all delivery addresses of the specified user, with the default address first."
    )
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

    @Operation(
            summary = "Update a user address",
            description = "Updates a delivery address owned by the specified user. Setting defaultAddress to true makes it the default address."
    )
    @PutMapping("/{addressId}")
    public ResponseEntity<ApiResponse<UserAddressResponse>> updateAddress (
            @PathVariable UUID userId,
            @PathVariable UUID addressId,
            @Valid @RequestBody UserAddressRequest userAddressRequest
    ){

        return  success(
                HttpStatus.OK,
                "User address updated successfully!",
                userAddressService.updateAddress(userId, addressId, userAddressRequest)
        );
    }

    @Operation(
            summary = "Delete a user address",
            description = "Permanently removes a delivery address owned by the specified user."
    )
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
