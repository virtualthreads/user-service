package com.aeropelican.userservice.repository;

import com.aeropelican.userservice.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {
        boolean existsByRoleNameIgnoreCase(String roleName);
        Optional<Role> findByRoleNameIgnoreCase(String roleName);
        boolean existsByRoleId(UUID roleId);
        Optional<Role> findByRoleId(UUID roleId);
}
