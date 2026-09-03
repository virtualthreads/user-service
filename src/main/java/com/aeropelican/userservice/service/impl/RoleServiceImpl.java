package com.aeropelican.userservice.service.impl;

import com.aeropelican.userservice.dto.request.CreateRoleRequest;
import com.aeropelican.userservice.dto.response.RoleResponse;
import com.aeropelican.userservice.entity.Role;
import com.aeropelican.userservice.exception.BusinessException;
import com.aeropelican.userservice.exception.InvalidRequestException;
import com.aeropelican.userservice.exception.ResourceAlreadyExistsException;
import com.aeropelican.userservice.exception.ResourceNotFoundException;
import com.aeropelican.userservice.mapper.RoleMapper;
import com.aeropelican.userservice.repository.RoleRepository;
import com.aeropelican.userservice.repository.UserRoleRepository;
import com.aeropelican.userservice.service.RoleService;
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
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleMapper roleMapper;

    @Override
    @Transactional
    public RoleResponse createRole(CreateRoleRequest request) {
        log.info("Creating role: {}", request.roleName());
        if (request == null || request.roleName() == null || request.roleName().isBlank()) {
            throw new InvalidRequestException("Role name is mandatory");
        }
        if (roleRepository.existsByRoleName(request.roleName().trim())) {
            throw new ResourceAlreadyExistsException("Role already exists with name: " + request.roleName());
        }

        Role role = roleMapper.toEntity(request);
        role.setRoleName(request.roleName().trim());
        Role saved = roleRepository.save(role);
        return roleMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "Get All Roles", key = "'all'")
    public List<RoleResponse> getAllRoles() {
        return roleRepository.findAll().stream().map(roleMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public RoleResponse getRoleById(UUID roleId) {
        if (roleId == null) {
            throw new InvalidRequestException("Role id is required");
        }
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + roleId));
        return roleMapper.toResponse(role);
    }

    @Override
    @Transactional
    public RoleResponse updateRole(UUID roleId, CreateRoleRequest request) {
        log.info("Updating role {}", roleId);
        if (roleId == null) {
            throw new InvalidRequestException("Role id is required");
        }
        if (request == null || request.roleName() == null || request.roleName().isBlank()) {
            throw new InvalidRequestException("Role name is mandatory");
        }

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + roleId));

        String normalizedName = request.roleName().trim();
        if (!role.getRoleName().equals(normalizedName) && roleRepository.existsByRoleName(normalizedName)) {
            throw new ResourceAlreadyExistsException("Role already exists with name: " + normalizedName);
        }

        role.setRoleName(normalizedName);
        role.setDescription(request.description());
        return roleMapper.toResponse(roleRepository.save(role));
    }

    @Override
    @Transactional
    public void deleteRole(UUID roleId) {
        log.info("Deleting role {}", roleId);
        if (roleId == null) throw new InvalidRequestException("Role id is required");
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + roleId));
        if (userRoleRepository.existsByRole(role)) {
            throw new BusinessException("Role is assigned to users and cannot be deleted");
        }
        roleRepository.delete(role);
    }
}
