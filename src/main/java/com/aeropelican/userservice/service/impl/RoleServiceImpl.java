package com.aeropelican.userservice.service.impl;

import com.aeropelican.userservice.dto.CreateRoleRequest;
import com.aeropelican.userservice.dto.RoleResponse;
import com.aeropelican.userservice.dto.UpdateRoleRequest;
import com.aeropelican.userservice.entity.Role;
import com.aeropelican.userservice.exception.ResourceAlreadyExistsException;
import com.aeropelican.userservice.exception.ResourceInUseException;
import com.aeropelican.userservice.exception.ResourceNotFoundException;
import com.aeropelican.userservice.mapper.RoleMapper;
import com.aeropelican.userservice.repository.RoleRepository;
import com.aeropelican.userservice.repository.UserRoleRepository;
import com.aeropelican.userservice.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    private static final Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);

    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleMapper roleMapper;

    public RoleServiceImpl(RoleRepository roleRepository, UserRoleRepository userRoleRepository, RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
        this.roleMapper = roleMapper;
    }

    @Override
    public RoleResponse createRole(CreateRoleRequest request) {
        logger.info("Initiating role creation for roleName: {}", request.roleName());

        if (roleRepository.existsByRoleName(request.roleName())) {
            logger.warn("Role creation failed: Role name '{}' already exists", request.roleName());
            throw new ResourceAlreadyExistsException("Role already exists with name: " + request.roleName());
        }

        Role roleEntity = roleMapper.toEntity(request);
        Role savedRole = roleRepository.save(roleEntity);

        logger.info("Successfully created role with ID: {}", savedRole.getRoleId());
        return roleMapper.toResponse(savedRole);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleResponse> getAllRoles() {
        logger.debug("Fetching all roles");
        return roleRepository.findAll().stream()
                .map(roleMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public RoleResponse getRoleById(UUID roleId) {
        logger.debug("Fetching role details for roleId: {}", roleId);
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> {
                    logger.warn("Role search failed: Role not found with ID: {}", roleId);
                    return new ResourceNotFoundException("Role not found with id: " + roleId);
                });
        return roleMapper.toResponse(role);
    }

    @Override
    public RoleResponse updateRole(UUID roleId, UpdateRoleRequest request) {
        logger.info("Updating role for ID: {}", roleId);

        Role existingRole = roleRepository.findById(roleId)
                .orElseThrow(() -> {
                    logger.warn("Role update failed: Role not found with ID: {}", roleId);
                    return new ResourceNotFoundException("Role not found with id: " + roleId);
                });

        if (roleRepository.existsByRoleNameAndRoleIdNot(request.roleName(), roleId)) {
            logger.warn("Role update failed: Role name '{}' already exists for another role", request.roleName());
            throw new ResourceAlreadyExistsException("Role already exists with name: " + request.roleName());
        }

        roleMapper.updateEntityFromDto(request, existingRole);
        Role updatedRole = roleRepository.save(existingRole);

        logger.info("Successfully updated role ID: {}", roleId);
        return roleMapper.toResponse(updatedRole);
    }

    @Override
    public void deleteRole(UUID roleId) {
        logger.info("Request received to delete role ID: {}", roleId);

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> {
                    logger.warn("Role delete failed: Role not found with ID: {}", roleId);
                    return new ResourceNotFoundException("Role not found with id: " + roleId);
                });

        if (userRoleRepository.existsByRole_RoleId(roleId)) {
            logger.warn("Role delete conflict: Role ID {} is assigned to users", roleId);
            throw new ResourceInUseException("Cannot delete role as it is assigned to one or more users");
        }

        roleRepository.delete(role);
        logger.info("Successfully deleted role ID: {}", roleId);
    }
}
