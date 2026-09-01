package com.huysg136.cirquo_server.user.repository;

import com.huysg136.cirquo_server.user.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PermissionRepository extends JpaRepository<Permission, UUID> {
}
