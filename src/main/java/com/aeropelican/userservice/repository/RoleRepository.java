package com.aeropelican.userservice.repository;

import com.aeropelican.userservice.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
    boolean existsByRoleName(String roleName);

    Optional<Role> findByRoleName(String roleName);
}
