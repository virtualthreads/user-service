// RoleRepository.java
package com.aeropelican.userservice.repository;

import com.aeropelican.userservice.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, String> {
    boolean existsByRoleName(String roleName);
    Optional<Role> findByRoleName(String roleName);
}