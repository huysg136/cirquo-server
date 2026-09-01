package com.huysg136.cirquo_server.user.mapper;

import com.huysg136.cirquo_server.user.dto.request.UserAddressRequest;
import com.huysg136.cirquo_server.user.dto.response.UserAddressResponse;
import com.huysg136.cirquo_server.user.entity.UserAddress;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserAddressMapper {
    UserAddressResponse toResponse(UserAddress userAddress);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    UserAddress toEntity(UserAddressRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(UserAddressRequest request, @MappingTarget UserAddress address);

}
