package com.aeropelican.userservice.repository;

import com.aeropelican.userservice.entity.Role;
import com.aeropelican.userservice.entity.User;
import com.aeropelican.userservice.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UUID> {
    boolean existsByUserAndRole(User user, Role role);

    List<UserRole> findByUser(User user);

    @Query("""
            SELECT ur.role
            FROM UserRole ur
            WHERE ur.user.userId = :userId
            ORDER BY ur.assignedAt
            """)
    List<Role> findRolesByUserId(@Param("userId") UUID userId);

    Optional<UserRole> findByUserAndRole(User user, Role role);

    boolean existsByRole(Role role);
}
