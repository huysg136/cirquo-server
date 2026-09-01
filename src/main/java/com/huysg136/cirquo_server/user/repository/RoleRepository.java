package com.huysg136.cirquo_server.user.repository;

import com.huysg136.cirquo_server.user.entity.Role;
import com.huysg136.cirquo_server.user.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {
    Optional<Role> findByName(RoleName name);
}
