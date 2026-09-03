package com.aeropelican.userservice.service.impl;

import com.aeropelican.userservice.dto.request.AssignRoleRequest;
import com.aeropelican.commonsservice.user.dto.response.RoleResponse;
import com.aeropelican.userservice.entity.Role;
import com.aeropelican.userservice.entity.User;
import com.aeropelican.userservice.entity.UserRole;
import com.aeropelican.userservice.exception.ResourceAlreadyExistsException;
import com.aeropelican.userservice.repository.RoleRepository;
import com.aeropelican.userservice.repository.UserRepository;
import com.aeropelican.userservice.repository.UserRoleRepository;
import com.aeropelican.userservice.service.UserRoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserRoleServiceImpl implements UserRoleService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;

    @Override
    @Transactional
    public RoleResponse assignRole(UUID userId, AssignRoleRequest request) {
        if (userId == null) throw new com.aeropelican.commonsservice.exception.InvalidRequestException("User id is required");
        if (request == null || request.roleId() == null) {
            throw new com.aeropelican.commonsservice.exception.InvalidRequestException("Role id is mandatory");
        }
        log.info("Assigning role {} to user {}", request.roleId(), userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new com.aeropelican.commonsservice.exception.ResourceNotFoundException("User not found with id: " + userId));
        Role role = roleRepository.findById(request.roleId())
                .orElseThrow(() -> new com.aeropelican.commonsservice.exception.ResourceNotFoundException("Role not found with id: " + request.roleId()));

        if (userRoleRepository.existsByUserAndRole(user, role)) {
            throw new ResourceAlreadyExistsException("Role already assigned to user");
        }

        UserRole userRole = UserRole.builder().user(user).role(role).build();
        userRoleRepository.save(userRole);

        return new RoleResponse(role.getRoleId(), role.getRoleName(), role.getDescription());
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "Get User Roles", key = "#userId")
    public List<RoleResponse> getUserRoles(UUID userId) {
        log.info("Fetching roles for user {}", userId);
        if (userId == null) {
            throw new com.aeropelican.commonsservice.exception.InvalidRequestException("User id is required");
        }
        if (!userRepository.existsById(userId)) {
            throw new com.aeropelican.commonsservice.exception.ResourceNotFoundException("User not found with id: " + userId);
        }
        return userRoleRepository.findRolesByUserId(userId).stream()
                .map(role -> new RoleResponse(role.getRoleId(), role.getRoleName(), role.getDescription()))
                .toList();
    }

    @Override
    @Transactional
    public void removeRole(UUID userId, UUID roleId) {
        log.info("Removing role {} from user {}", roleId, userId);
        if (userId == null) throw new com.aeropelican.commonsservice.exception.InvalidRequestException("User id is required");
        if (roleId == null) throw new com.aeropelican.commonsservice.exception.InvalidRequestException("Role id is required");

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new com.aeropelican.commonsservice.exception.ResourceNotFoundException("User not found with id: " + userId));
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new com.aeropelican.commonsservice.exception.ResourceNotFoundException("Role not found with id: " + roleId));

        UserRole mapping = userRoleRepository.findByUserAndRole(user, role)
                .orElseThrow(() -> new com.aeropelican.commonsservice.exception.ResourceNotFoundException("Role mapping not found for user and role"));
        userRoleRepository.delete(mapping);
    }
}
