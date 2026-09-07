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
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(
        name = "Addresses",
        description = "Manage the authenticated user's delivery addresses"
)
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/v1/addresses")
@RequiredArgsConstructor
public class UserAddressController extends BaseController {

    private final UserAddressService userAddressService;

    @Operation(
            summary = "Create an address",
            description = "Creates a delivery address for the authenticated user."
    )
    @PostMapping
    public ResponseEntity<ApiResponse<UserAddressResponse>> createAddress(
            Authentication authentication,
            @Valid @RequestBody UserAddressRequest request
    ) {
        return success(
                HttpStatus.CREATED,
                "User address created successfully!",
                userAddressService.createAddress(
                        getCurrentUserId(authentication),
                        request
                )
        );
    }

    @Operation(
            summary = "Get current user's addresses",
            description = "Returns the authenticated user's delivery addresses with the default first."
    )
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserAddressResponse>>> getAddresses(
            Authentication authentication
    ) {
        return success(
                HttpStatus.OK,
                "User addresses retrieved successfully!",
                userAddressService.getAllByUserId(
                        getCurrentUserId(authentication)
                )
        );
    }

    @Operation(
            summary = "Update an address",
            description = "Updates an address owned by the authenticated user."
    )
    @PutMapping("/{addressId}")
    public ResponseEntity<ApiResponse<UserAddressResponse>> updateAddress(
            Authentication authentication,
            @PathVariable UUID addressId,
            @Valid @RequestBody UserAddressRequest request
    ) {
        return success(
                HttpStatus.OK,
                "User address updated successfully!",
                userAddressService.updateAddress(
                        getCurrentUserId(authentication),
                        addressId,
                        request
                )
        );
    }

    @Operation(
            summary = "Delete an address",
            description = "Permanently removes an address owned by the authenticated user."
    )
    @DeleteMapping("/{addressId}")
    public ResponseEntity<ApiResponse<Void>> deleteAddress(
            Authentication authentication,
            @PathVariable UUID addressId
    ) {
        userAddressService.deleteAddress(
                getCurrentUserId(authentication),
                addressId
        );

        return success(
                HttpStatus.OK,
                "User address deleted successfully!",
                null
        );
    }

    private UUID getCurrentUserId(Authentication authentication) {
        return UUID.fromString(authentication.getName());
    }
}
