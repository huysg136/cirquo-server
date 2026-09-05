package com.huysg136.cirquo_server.user.repository;

import com.huysg136.cirquo_server.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    @Query("""
        SELECT u
        FROM User u
        JOIN FETCH u.role
    """)
    List<User> findAllWithRole();

    @Query("""
        SELECT u
        FROM User u
        JOIN FETCH u.role
        WHERE u.id = :userId
    """)
    Optional<User> findByIdWithRole(@Param("userId") UUID userId);
}
