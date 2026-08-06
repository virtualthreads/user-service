package com.aeropelican.userservice.service;

import com.aeropelican.userservice.dto.request.CreateRoleRequest;
import com.aeropelican.userservice.dto.response.RoleResponse;
import com.aeropelican.userservice.entity.Role;
import com.aeropelican.userservice.entity.UserRole;
import com.aeropelican.userservice.exception.ResourceAlreadyExistsException;
import com.aeropelican.userservice.exception.ResourceInUseException;
import com.aeropelican.userservice.exception.ResourceNotFoundException;
import com.aeropelican.userservice.mapper.RoleMapper;
import com.aeropelican.userservice.repository.RoleRepository;
import com.aeropelican.userservice.repository.UserRepository;
import com.aeropelican.userservice.repository.UserRoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class RoleService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleMapper roleMapper;

    public RoleService(RoleRepository roleRepository,
                       UserRepository userRepository,
                       UserRoleRepository userRoleRepository,
                       RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.roleMapper = roleMapper;
    }

    @Transactional
    public RoleResponse createRole(CreateRoleRequest request) {
        if (roleRepository.existsByRoleName(request.roleName())) {
            throw new ResourceAlreadyExistsException("Role already exists with name: " + request.roleName());
        }

        Role role = roleMapper.toEntity(request);
        Role savedRole = roleRepository.save(role);
        return roleMapper.toResponse(savedRole);
    }

    public List<RoleResponse> getAllRoles() {
        return roleRepository.findAll()
                .stream()
                .map(roleMapper::toResponse)
                .toList();
    }

    @Transactional
    public void deleteRole(String roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));

        if (userRoleRepository.existsByRoleId(roleId)) {
            throw new ResourceInUseException("Cannot delete role as it is assigned to one or more users");
        }

        roleRepository.delete(role);
    }

    @Transactional
    public void assignRoleToUser(String userId, String roleId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found");
        }

        if (!roleRepository.existsById(roleId)) {
            throw new ResourceNotFoundException("Role not found");
        }

        if (userRoleRepository.existsByUserIdAndRoleId(userId, roleId)) {
            throw new ResourceAlreadyExistsException("Role already assigned to user");
        }

        UserRole userRole = new UserRole();
        userRole.setUserRoleId(UUID.randomUUID().toString());
        userRole.setUserId(userId);
        userRole.setRoleId(roleId);

        userRoleRepository.save(userRole);
    }

    @Transactional
    public void removeRoleFromUser(String userId, String roleId) {
        UserRole userRole = userRoleRepository.findByUserIdAndRoleId(userId, roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role assignment not found for given user"));

        userRoleRepository.delete(userRole);
    }
}