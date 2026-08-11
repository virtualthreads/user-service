package com.aeropelican.userservice.service.impl;

import com.aeropelican.userservice.dto.request.UserRoleCreateRequestDTO;
import com.aeropelican.userservice.dto.request.UserRoleUpdateRequestDTO;
import com.aeropelican.userservice.dto.response.UserRoleResponse;
import com.aeropelican.userservice.entity.Role;
import com.aeropelican.userservice.entity.User;
import com.aeropelican.userservice.entity.UserRole;
import com.aeropelican.userservice.exception.RoleNotFoundException;
import com.aeropelican.userservice.exception.UserNotFoundException;
import com.aeropelican.userservice.exception.UserRoleNotFoundException;
import com.aeropelican.userservice.mapper.UserRoleMapper;
import com.aeropelican.userservice.repository.RoleRepository;
import com.aeropelican.userservice.repository.UserRepository;
import com.aeropelican.userservice.repository.UserRoleRepository;
import com.aeropelican.userservice.service.UserRoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserRoleServiceImpl implements UserRoleService {

    private final UserRoleRepository userRoleRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public UserRoleResponse assignRole(UserRoleCreateRequestDTO request) {

        log.info("Assigning role {} to user {}",
                request.roleId(), request.userId());

        User user = userRepository.findById(request.userId())
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found with id: " + request.userId()));

        Role role = roleRepository.findById(request.roleId())
                .orElseThrow(() ->
                        new RoleNotFoundException(
                                "Role not found with id: " + request.roleId()));

        if (userRoleRepository.existsByUserUserIdAndRoleRoleId(
                request.userId(),
                request.roleId())) {

            throw new RuntimeException("User already has this role.");
        }

        UserRole userRole = UserRoleMapper.toEntity(user, role);

        userRoleRepository.save(userRole);

        log.info("Role assigned successfully with id: {}",
                userRole.getUserRoleId());

        return UserRoleMapper.toResponse(userRole);
    }

    @Override
    public UserRoleResponse getUserRoleById(String id) {

        log.info("Fetching UserRole with id: {}", id);

        UserRole userRole = userRoleRepository.findById(id)
                .orElseThrow(() ->
                        new UserRoleNotFoundException(
                                "UserRole not found with id: " + id));

        log.info("UserRole fetched successfully");

        return UserRoleMapper.toResponse(userRole);
    }

    @Override
    public Page<UserRoleResponse> getAllUserRoles(int page, int size) {

        log.info("Fetching UserRoles. Page: {}, Size: {}", page, size);

        Pageable pageable = PageRequest.of(page, size);

        Page<UserRoleResponse> response = userRoleRepository
                .findAll(pageable)
                .map(UserRoleMapper::toResponse);

        log.info("Fetched {} user roles",
                response.getNumberOfElements());

        return response;
    }

    @Override
    public UserRoleResponse updateUserRole(
            String id,
            UserRoleUpdateRequestDTO request) {

        log.info("Updating UserRole with id: {}", id);

        UserRole userRole = userRoleRepository.findById(id)
                .orElseThrow(() ->
                        new UserRoleNotFoundException(
                                "UserRole not found with id: " + id));

        Role role = roleRepository.findById(request.roleId())
                .orElseThrow(() ->
                        new RoleNotFoundException(
                                "Role not found with id: " + request.roleId()));

        UserRoleMapper.updateEntity(userRole, role);

        userRoleRepository.save(userRole);

        log.info("UserRole updated successfully with id: {}",
                userRole.getUserRoleId());

        return UserRoleMapper.toResponse(userRole);
    }

    @Override
    public void deleteUserRole(String id) {

        log.info("Deleting UserRole with id: {}", id);

        UserRole userRole = userRoleRepository.findById(id)
                .orElseThrow(() ->
                        new UserRoleNotFoundException(
                                "UserRole not found with id: " + id));

        userRoleRepository.delete(userRole);

        log.info("UserRole deleted successfully with id: {}", id);
    }
}