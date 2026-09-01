package com.huysg136.cirquo_server.user.service.impl;

import com.huysg136.cirquo_server.user.dto.request.UserAddressRequest;
import com.huysg136.cirquo_server.user.dto.response.UserAddressResponse;
import com.huysg136.cirquo_server.user.entity.User;
import com.huysg136.cirquo_server.user.entity.UserAddress;
import com.huysg136.cirquo_server.user.exception.UserAddressNotFoundException;
import com.huysg136.cirquo_server.user.exception.UserNotFoundException;
import com.huysg136.cirquo_server.user.mapper.UserAddressMapper;
import com.huysg136.cirquo_server.user.repository.UserAddressRepository;
import com.huysg136.cirquo_server.user.repository.UserRepository;
import com.huysg136.cirquo_server.user.service.UserAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserAddressServiceImpl implements UserAddressService {
    private final UserAddressMapper userAddressMapper;
    private final UserRepository userRepository;
    private final UserAddressRepository userAddressRepository;

    @Transactional
    @Override
    public UserAddressResponse createAddress(UUID userId, UserAddressRequest userAddressRequest) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        if (userAddressRequest.defaultAddress()){
            userAddressRepository.clearDefaultByUserId(userId);
        }
        UserAddress userAddress = userAddressMapper.toEntity(userAddressRequest);
        userAddress.setUser(user);

        UserAddress savedAddress = userAddressRepository.save(userAddress);
        return userAddressMapper.toResponse(savedAddress);
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserAddressResponse> getAllByUserId(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException();
        }

        return userAddressRepository
                .findAllByUserIdOrderByDefaultAddressDescCreatedAtDesc(userId)
                .stream()
                .map(userAddressMapper::toResponse)
                .toList();
    }

    @Transactional
    @Override
    public UserAddressResponse updateAddress(UUID userId, UUID addressId, UserAddressRequest userAddressRequest) {
        UserAddress userAddress = userAddressRepository.findByIdAndUserId(addressId, userId)
                .orElseThrow(UserAddressNotFoundException::new);

        if (userAddressRequest.defaultAddress()){
            userAddressRepository.clearDefaultExcept(userId, addressId);
        }

        userAddressMapper.updateEntity(userAddressRequest, userAddress);

        UserAddress savedAddress = userAddressRepository.save(userAddress);
        return userAddressMapper.toResponse(savedAddress);
    }

    @Transactional
    @Override
    public void deleteAddress(UUID userId, UUID addressId) {
        UserAddress userAddress = userAddressRepository.findByIdAndUserId(addressId, userId)
                .orElseThrow(UserAddressNotFoundException::new);

        userAddressRepository.delete(userAddress);
    }
}
