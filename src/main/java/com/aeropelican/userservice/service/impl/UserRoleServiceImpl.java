package com.aeropelican.userservice.service.impl;

import com.aeropelican.userservice.dto.request.AssignRoleRequestDTO;
import com.aeropelican.userservice.dto.response.UserRoleResponseDTO;
import com.aeropelican.userservice.entity.Role;
import com.aeropelican.userservice.entity.UserRole;
import com.aeropelican.userservice.exception.RoleNotFoundException;
import com.aeropelican.userservice.exception.UserNotFoundException;
import com.aeropelican.userservice.exception.UserRoleAlreadyExistsException;
import com.aeropelican.userservice.exception.UserRoleNotFoundException;
import com.aeropelican.userservice.repository.RoleRepository;
import com.aeropelican.userservice.repository.UserRepository;
import com.aeropelican.userservice.repository.UserRoleRepository;
import com.aeropelican.userservice.service.UserRoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserRoleServiceImpl implements UserRoleService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;

    // =========================================================
    // ASSIGN ROLE TO USER
    // =========================================================

    @Override
    public UserRoleResponseDTO assignRole(
            UUID userId,
            AssignRoleRequestDTO request) {

        log.info(
                "Assigning role {} to user {}",
                request.getRoleId(),
                userId
        );

        // Check whether user exists
        userRepository.findById(userId)
                .orElseThrow(() -> {

                    log.warn(
                            "User not found while assigning role: userId={}",
                            userId
                    );

                    return new UserNotFoundException(
                            "User not found with ID: " + userId
                    );
                });

        // Check whether role exists
        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> {

                    log.warn(
                            "Role not found while assigning role: roleId={}",
                            request.getRoleId()
                    );

                    return new RoleNotFoundException(
                            "Role not found with ID: "
                                    + request.getRoleId()
                    );
                });

        // Check whether role is already assigned
        if (userRoleRepository.existsByUserIdAndRoleId(
                userId,
                request.getRoleId())) {

            log.warn(
                    "Role already assigned: userId={}, roleId={}",
                    userId,
                    request.getRoleId()
            );

            throw new UserRoleAlreadyExistsException(
                    "Role is already assigned to this user"
            );
        }

        // Create user-role mapping
        UserRole userRole = UserRole.builder()
                .userId(userId)
                .roleId(request.getRoleId())
                .build();

        // Save mapping
        UserRole savedUserRole =
                userRoleRepository.save(userRole);

        log.info(
                "Role assigned successfully: userId={}, roleId={}",
                userId,
                request.getRoleId()
        );

        return toResponse(
                savedUserRole,
                role
        );
    }

    // =========================================================
    // GET USER ROLES
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<UserRoleResponseDTO> getUserRoles(
            UUID userId) {

        log.info(
                "Fetching roles for user: {}",
                userId
        );

        // Check whether user exists
        userRepository.findById(userId)
                .orElseThrow(() -> {

                    log.warn(
                            "User not found while fetching roles: userId={}",
                            userId
                    );

                    return new UserNotFoundException(
                            "User not found with ID: " + userId
                    );
                });

        // Find user-role mappings
        List<UserRole> userRoles =
                userRoleRepository.findByUserId(userId);

        // Convert mappings to response DTOs
        return userRoles.stream()
                .map(userRole -> {

                    Role role = roleRepository.findById(
                            userRole.getRoleId()
                    ).orElseThrow(() -> {

                        log.error(
                                "Role referenced by user-role mapping " +
                                        "does not exist: roleId={}",
                                userRole.getRoleId()
                        );

                        return new RoleNotFoundException(
                                "Role not found with ID: "
                                        + userRole.getRoleId()
                        );
                    });

                    return toResponse(
                            userRole,
                            role
                    );
                })
                .toList();
    }

    // =========================================================
    // REMOVE ROLE FROM USER
    // =========================================================

    @Override
    public void removeRole(
            UUID userId,
            UUID roleId) {

        log.info(
                "Removing role {} from user {}",
                roleId,
                userId
        );

        // Check whether user exists
        userRepository.findById(userId)
                .orElseThrow(() -> {

                    log.warn(
                            "User not found while removing role: userId={}",
                            userId
                    );

                    return new UserNotFoundException(
                            "User not found with ID: "
                                    + userId
                    );
                });

        // Check whether role exists
        roleRepository.findById(roleId)
                .orElseThrow(() -> {

                    log.warn(
                            "Role not found while removing role: roleId={}",
                            roleId
                    );

                    return new RoleNotFoundException(
                            "Role not found with ID: "
                                    + roleId
                    );
                });

        // Check whether the mapping exists
        userRoleRepository
                .findByUserIdAndRoleId(
                        userId,
                        roleId
                )
                .orElseThrow(() -> {

                    log.warn(
                            "Role mapping not found: userId={}, roleId={}",
                            userId,
                            roleId
                    );

                    return new UserRoleNotFoundException(
                            "Role is not assigned to this user"
                    );
                });

        // Delete mapping
        userRoleRepository.deleteByUserIdAndRoleId(
                userId,
                roleId
        );

        log.info(
                "Role removed successfully: userId={}, roleId={}",
                userId,
                roleId
        );
    }

    // =========================================================
    // CONVERT ENTITY TO RESPONSE DTO
    // =========================================================

    private UserRoleResponseDTO toResponse(
            UserRole userRole,
            Role role) {

        return UserRoleResponseDTO.builder()
                .userId(userRole.getUserId())
                .roleId(userRole.getRoleId())
                .roleName(role.getRoleName())
                .assignedAt(userRole.getAssignedAt())
                .build();
    }
}