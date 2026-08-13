package com.aeropelican.userservice.repository;

import com.aeropelican.userservice.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UUID> {

    boolean existsByUser_UserIdAndRole_RoleId(UUID userId, UUID roleId);

    boolean existsByRole_RoleId(UUID roleId);

    List<UserRole> findByUser_UserId(UUID userId);

    Optional<UserRole> findByUser_UserIdAndRole_RoleId(UUID userId, UUID roleId);

    void deleteByUser_UserIdAndRole_RoleId(UUID userId, UUID roleId);
}
