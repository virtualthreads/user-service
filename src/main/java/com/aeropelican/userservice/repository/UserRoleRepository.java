package com.aeropelican.userservice.repository;

import com.aeropelican.userservice.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRoleRepository
        extends JpaRepository<UserRole, UUID> {

    // =========================================================
    // CHECK ROLE ASSIGNMENT
    // =========================================================

    boolean existsByUserIdAndRoleId(
            UUID userId,
            UUID roleId
    );

    // =========================================================
    // CHECK ROLE USAGE
    // =========================================================

    boolean existsByRoleId(UUID roleId);

    // =========================================================
    // GET USER ROLES
    // =========================================================

    List<UserRole> findByUserId(UUID userId);

    // =========================================================
    // FIND SPECIFIC USER ROLE
    // =========================================================

    Optional<UserRole> findByUserIdAndRoleId(
            UUID userId,
            UUID roleId
    );

    // =========================================================
    // REMOVE USER ROLE
    // =========================================================

    void deleteByUserIdAndRoleId(
            UUID userId,
            UUID roleId
    );
}