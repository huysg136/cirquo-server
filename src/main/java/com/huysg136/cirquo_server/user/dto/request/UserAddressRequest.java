package com.huysg136.cirquo_server.user.dto.request;

import com.huysg136.cirquo_server.annotation.PhoneNumber;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserAddressRequest(
        @NotBlank(message = "Recipient name is required!")
        @Size(max = 255, message = "Recipient name must not exceed 255 characters!")
        String recipientName,

        @NotBlank(message = "Phone is required!")
        @PhoneNumber
        String phone,

        @NotBlank(message = "Province is required!")
        @Size(max = 100, message = "Province must not exceed 100 characters!")
        String province,

        @NotBlank(message = "Ward is required!")
        @Size(max = 100, message = "Ward must not exceed 100 characters!")
        String ward,

        @NotBlank(message = "Address line is required!")
        @Size(max = 255, message = "Address line must not exceed 255 characters!")
        String addressLine,

        boolean defaultAddress
) {
}
