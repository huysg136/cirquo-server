package com.huysg136.cirquo_server.user.repository;

import com.huysg136.cirquo_server.user.entity.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserAddressRepository extends JpaRepository<UserAddress, UUID> {
    List<UserAddress> findAllByUserIdOrderByDefaultAddressDescCreatedAtDesc(UUID userId);

    Optional<UserAddress> findByIdAndUserId(UUID addressId, UUID userId);

    @Modifying
    @Query("""
        UPDATE UserAddress address
        SET address.defaultAddress = false
        WHERE address.user.id = :userId
          AND address.defaultAddress = true
    """)
    void clearDefaultByUserId(@Param("userId") UUID userId);

    @Modifying
    @Query("""
        UPDATE UserAddress address
        SET address.defaultAddress = false
        WHERE address.user.id = :userId
          AND address.id <> :addressId
          AND address.defaultAddress = true
    """)
    void clearDefaultExcept(
            @Param("userId") UUID userId,
            @Param("addressId") UUID addressId
    );
}
