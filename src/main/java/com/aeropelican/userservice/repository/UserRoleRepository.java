package com.aeropelican.userservice.repository;

import com.aeropelican.userservice.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, String> {

    List<UserRole> findByUserUserId(String userId);

    List<UserRole> findByRoleRoleId(String roleId);
    boolean existsByUserUserIdAndRoleRoleId(
            String userId,
            String roleId);
}