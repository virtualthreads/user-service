package com.aeropelican.userservice.repository;

import com.aeropelican.userservice.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRoleRepository extends JpaRepository<UserRole, UUID> {

    boolean existsByRoleId(UUID roleId);
}