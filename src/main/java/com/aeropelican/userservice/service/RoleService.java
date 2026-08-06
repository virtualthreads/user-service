package com.aeropelican.userservice.service;

import com.aeropelican.userservice.dto.Request.CreateRoleRequest;
import com.aeropelican.userservice.dto.Response.RoleResponse;
import com.aeropelican.userservice.entity.Role;
import com.aeropelican.userservice.entity.UserRole;
import com.aeropelican.userservice.exception.ResourceAlreadyExistsException;
import com.aeropelican.userservice.exception.ResourceInUseException;
import com.aeropelican.userservice.exception.ResourceNotFoundException;
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
    private final UserRoleRepository userRoleRepository;
    private final UserRepository userRepository;

    public RoleService(RoleRepository roleRepository, UserRoleRepository userRoleRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public RoleResponse createRole(CreateRoleRequest request) {
        if (roleRepository.existsByRoleName(request.roleName())) {
            throw new ResourceAlreadyExistsException("Role already exists: " + request.roleName());
        }

        Role role = new Role();
        role.setRoleId(UUID.randomUUID().toString());
        role.setRoleName(request.roleName());
        role.setDescription(request.description());

        Role saved = roleRepository.save(role);
        return new RoleResponse(saved.getRoleId(), saved.getRoleName(), saved.getDescription());
    }

    public List<RoleResponse> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(r -> new RoleResponse(r.getRoleId(), r.getRoleName(), r.getDescription()))
                .toList();
    }

    @Transactional
    public void deleteRole(String roleId) {
        if (!roleRepository.existsById(roleId)) {
            throw new ResourceNotFoundException("Role not found");
        }
        if (userRoleRepository.existsByRoleId(roleId)) {
            throw new ResourceInUseException("Cannot delete role that is assigned to users");
        }
        roleRepository.deleteById(roleId);
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
                .orElseThrow(() -> new ResourceNotFoundException("Role assignment not found"));
        userRoleRepository.delete(userRole);
    }
}