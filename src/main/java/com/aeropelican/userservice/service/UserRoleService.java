package com.aeropelican.userservice.service;

import com.aeropelican.userservice.dto.request.AssignRoleRequestDTO;
import com.aeropelican.userservice.dto.response.UserRoleResponseDTO;
import com.aeropelican.userservice.entity.Role;
import com.aeropelican.userservice.entity.User;
import com.aeropelican.userservice.entity.UserRole;
import com.aeropelican.userservice.exceptions.MappingAlreadyExistsException;
import com.aeropelican.userservice.exceptions.ResourceNotFoundException;
import com.aeropelican.userservice.mapper.UserRoleMapper;
import com.aeropelican.userservice.repository.RoleRepository;
import com.aeropelican.userservice.repository.UserRepository;
import com.aeropelican.userservice.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserRoleService {

    private final UserRoleRepository userRoleRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
     //To Assign Role to User
    public UserRoleResponseDTO assignRole(UUID userId,
                                          AssignRoleRequestDTO request) {

        log.info("Assigning role {} to user {}", request.getRoleId(), userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found: {}", userId);
                    return new ResourceNotFoundException("User not found");
                });

        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> {
                    log.error("Role not found: {}", request.getRoleId());
                    return new ResourceNotFoundException("Role not found");
                });

        if (userRoleRepository.existsByUserIdAndRoleId(userId, request.getRoleId())) {
            log.warn("Role {} already assigned to user {}", request.getRoleId(), userId);
            throw new MappingAlreadyExistsException("Role already assigned to user.");
        }

        UserRole userRole = UserRole.builder()
                .userId(user.getUserId())
                .roleId(role.getRoleId())
                .build();

        UserRole saved = userRoleRepository.save(userRole);

        log.info("Role assigned successfully. Mapping Id: {}", saved.getUserRoleId());

        return UserRoleMapper.toResponseDTO(saved);
    }
     // Get Roles of User
    public List<UserRoleResponseDTO> getUserRoles(UUID userId) {

        log.info("Fetching roles for user {}", userId);

        if (!userRepository.existsById(userId)) {
            log.error("User not found: {}", userId);
            throw new ResourceNotFoundException("User not found");
        }

        List<UserRoleResponseDTO> response = userRoleRepository.findByUserId(userId)
                .stream()
                .map(UserRoleMapper::toResponseDTO)
                .collect(Collectors.toList());

        log.info("Fetched {} role(s) for user {}", response.size(), userId);

        return response;
    }
    //delete role
    public void removeRole(UUID userId,
                           UUID roleId) {

        log.info("Removing role {} from user {}", roleId, userId);

        if (!userRepository.existsById(userId)) {
            log.error("User not found: {}", userId);
            throw new ResourceNotFoundException("User not found");
        }

        if (!roleRepository.existsById(roleId)) {
            log.error("Role not found: {}", roleId);
            throw new ResourceNotFoundException("Role not found");
        }

        UserRole mapping = userRoleRepository
                .findByUserIdAndRoleId(userId, roleId)
                .orElseThrow(() -> {
                    log.error("UserRole mapping not found");
                    return new ResourceNotFoundException("User role mapping not found");
                });

        userRoleRepository.delete(mapping);

        log.info("Role {} removed successfully from user {}", roleId, userId);
    }

}
