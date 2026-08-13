package com.aeropelican.userservice.service.impl;

import com.aeropelican.userservice.dto.AssignRoleRequest;
import com.aeropelican.userservice.dto.RoleResponse;
import com.aeropelican.userservice.entity.Role;
import com.aeropelican.userservice.entity.Status;
import com.aeropelican.userservice.entity.User;
import com.aeropelican.userservice.entity.UserRole;
import com.aeropelican.userservice.exception.ResourceAlreadyExistsException;
import com.aeropelican.userservice.exception.ResourceNotFoundException;
import com.aeropelican.userservice.mapper.RoleMapper;
import com.aeropelican.userservice.repository.RoleRepository;
import com.aeropelican.userservice.repository.UserRepository;
import com.aeropelican.userservice.repository.UserRoleRepository;
import com.aeropelican.userservice.service.UserRoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class UserRoleServiceImpl implements UserRoleService {

    private static final Logger logger = LoggerFactory.getLogger(UserRoleServiceImpl.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleMapper roleMapper;

    public UserRoleServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
                               UserRoleRepository userRoleRepository, RoleMapper roleMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
        this.roleMapper = roleMapper;
    }

    @Override
    public RoleResponse assignRole(UUID userId, AssignRoleRequest request) {
        logger.info("Assigning role ID: {} to user ID: {}", request.roleId(), userId);

        User user = userRepository.findByUserIdAndStatusNot(userId, Status.DELETED)
                .orElseThrow(() -> {
                    logger.warn("Role assignment failed: Active user not found with ID: {}", userId);
                    return new ResourceNotFoundException("User not found with id: " + userId);
                });

        Role role = roleRepository.findById(request.roleId())
                .orElseThrow(() -> {
                    logger.warn("Role assignment failed: Role not found with ID: {}", request.roleId());
                    return new ResourceNotFoundException("Role not found with id: " + request.roleId());
                });

        if (userRoleRepository.existsByUser_UserIdAndRole_RoleId(userId, request.roleId())) {
            logger.warn("Role assignment conflict: Role ID {} is already assigned to user ID {}", request.roleId(), userId);
            throw new ResourceAlreadyExistsException("Role is already assigned to this user");
        }

        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(role);

        userRoleRepository.save(userRole);
        logger.info("Successfully assigned role ID {} to user ID {}", request.roleId(), userId);

        return roleMapper.toResponse(role);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleResponse> getUserRoles(UUID userId) {
        logger.debug("Fetching assigned roles for user ID: {}", userId);

        if (!userRepository.existsById(userId)) {
            logger.warn("Fetch roles failed: User not found with ID: {}", userId);
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }

        return userRoleRepository.findByUser_UserId(userId).stream()
                .map(UserRole::getRole)
                .map(roleMapper::toResponse)
                .toList();
    }

    @Override
    public void removeRole(UUID userId, UUID roleId) {
        logger.info("Removing role ID: {} from user ID: {}", roleId, userId);

        if (!userRepository.existsById(userId)) {
            logger.warn("Role removal failed: User not found with ID: {}", userId);
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }

        if (!roleRepository.existsById(roleId)) {
            logger.warn("Role removal failed: Role not found with ID: {}", roleId);
            throw new ResourceNotFoundException("Role not found with id: " + roleId);
        }

        UserRole userRole = userRoleRepository.findByUser_UserIdAndRole_RoleId(userId, roleId)
                .orElseThrow(() -> {
                    logger.warn("Role removal failed: Role mapping not found for user ID {} and role ID {}", userId, roleId);
                    return new ResourceNotFoundException("Role mapping does not exist for this user");
                });

        userRoleRepository.delete(userRole);
        logger.info("Successfully removed role ID {} from user ID {}", roleId, userId);
    }
}
