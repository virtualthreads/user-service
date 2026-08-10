package com.aeropelican.userservice.service;

import com.aeropelican.userservice.dto.request.UserRoleCreateRequestDTO;
import com.aeropelican.userservice.dto.request.UserRoleUpdateRequestDTO;
import com.aeropelican.userservice.dto.response.UserRoleResponseDTO;
import com.aeropelican.userservice.entity.Role;
import com.aeropelican.userservice.entity.User;
import com.aeropelican.userservice.entity.UserRole;
import com.aeropelican.userservice.exceptions.ResourceNotFoundException;
import com.aeropelican.userservice.mapper.UserRoleMapper;
import com.aeropelican.userservice.repository.RoleRepository;
import com.aeropelican.userservice.repository.UserRepository;
import com.aeropelican.userservice.repository.UserRoleRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserRoleService {

    private final UserRoleRepository userRoleRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleMapper userRoleMapper;

    public UserRoleResponseDTO createUserRole(
            UserRoleCreateRequestDTO requestDTO) {

        log.info("Creating user-role mapping");

        User user = userRepository.findById(requestDTO.getUserId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        Role role = roleRepository.findById(requestDTO.getRoleId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Role not found"));

        UserRole userRole = UserRole.builder()
                .userRoleId(UUID.randomUUID())
                .user(user)
                .role(role)
                .assignedAt(LocalDateTime.now())
                .build();

        return userRoleMapper.toResponseDTO(
                userRoleRepository.save(userRole));
    }

    public List<UserRoleResponseDTO> getAllUserRoles() {

        log.info("Fetching all user-role mappings");

        return userRoleRepository.findAll()
                .stream()
                .map(userRoleMapper::toResponseDTO)
                .toList();
    }

    public UserRoleResponseDTO getUserRoleById(UUID id) {

        log.info("Fetching user-role mapping with id: {}", id);

        UserRole userRole = userRoleRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "UserRole not found with id: " + id));

        return userRoleMapper.toResponseDTO(userRole);
    }

    public UserRoleResponseDTO updateUserRole(
            UUID id,
            UserRoleUpdateRequestDTO requestDTO) {

        log.info("Updating user-role mapping with id: {}", id);

        UserRole userRole = userRoleRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "UserRole not found with id: " + id));

        User user = userRepository.findById(requestDTO.getUserId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        Role role = roleRepository.findById(requestDTO.getRoleId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Role not found"));

        userRole.setUser(user);
        userRole.setRole(role);

        return userRoleMapper.toResponseDTO(
                userRoleRepository.save(userRole));
    }

    public void deleteUserRole(UUID id) {

        log.info("Deleting user-role mapping with id: {}", id);

        UserRole userRole = userRoleRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "UserRole not found with id: " + id));

        userRoleRepository.delete(userRole);
    }
}