package com.huysg136.cirquo_server.user.service;

import com.huysg136.cirquo_server.user.dto.request.UserAddressRequest;
import com.huysg136.cirquo_server.user.dto.response.UserAddressResponse;

import java.util.List;
import java.util.UUID;

public interface UserAddressService {
    UserAddressResponse createAddress(UUID userId, UserAddressRequest userAddressRequest);

    List<UserAddressResponse> getAllByUserId(UUID userId);

    UserAddressResponse updateAddress(UUID userId, UUID addressId,UserAddressRequest userAddressRequest);

    void deleteAddress(UUID userId, UUID addressId);
}
