package com.huysg136.cirquo_server.user.repository;

import com.huysg136.cirquo_server.user.entity.User;
import com.huysg136.cirquo_server.user.enums.RoleName;
import com.huysg136.cirquo_server.user.enums.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    @EntityGraph(attributePaths = "role")
    @Query("""
        SELECT u
        FROM User u
        WHERE (:status IS NULL OR u.status = :status)
          AND (:roleName IS NULL OR u.role.name = :roleName)
          AND (
              :keyword IS NULL
              OR LOWER(u.email) LIKE LOWER(CONCAT('%', CAST(:keyword AS string), '%'))
              OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', CAST(:keyword AS string), '%'))
          )
        """)
    Page<User> findForAdmin(
            @Param("status") UserStatus status,
            @Param("roleName") RoleName roleName,
            @Param("keyword") String keyword,
            Pageable pageable
    );

    @Query("""
        SELECT u
        FROM User u
        JOIN FETCH u.role
        WHERE u.id = :userId
    """)
    Optional<User> findByIdWithRole(@Param("userId") UUID userId);
}
