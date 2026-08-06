package com.aeropelican.userservice.service.impl;

import com.aeropelican.userservice.dto.request.CreateRoleRequestDTO;
import com.aeropelican.userservice.dto.request.UpdateRoleRequestDTO;
import com.aeropelican.userservice.dto.response.RoleResponseDTO;
import com.aeropelican.userservice.entity.Role;
import com.aeropelican.userservice.mapper.RoleMapper;
import com.aeropelican.userservice.repository.RoleRepository;
import com.aeropelican.userservice.repository.UserRoleRepository;
import com.aeropelican.userservice.service.RoleService;
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
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleMapper roleMapper;

    @Override
    public RoleResponseDTO createRole(CreateRoleRequestDTO request) {

        log.info("Role creation requested: roleName={}", request.getRoleName());

        if (roleRepository.existsByRoleName(request.getRoleName())) {

            log.warn(
                    "Role creation failed: role already exists, roleName={}",
                    request.getRoleName()
            );

            throw new IllegalArgumentException(
                    "Role with name '" + request.getRoleName() + "' already exists"
            );
        }

        log.debug(
                "Creating new role with roleName={}",
                request.getRoleName()
        );

        Role role = roleMapper.toEntity(request);

        Role savedRole = roleRepository.save(role);

        log.info(
                "Role created successfully: roleId={}, roleName={}",
                savedRole.getRoleId(),
                savedRole.getRoleName()
        );

        return roleMapper.toResponse(savedRole);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleResponseDTO> getAllRoles() {

        log.info("Fetching all roles");

        List<Role> roles = roleRepository.findAll();

        log.debug(
                "Total roles found: count={}",
                roles.size()
        );

        return roles.stream()
                .map(roleMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public RoleResponseDTO getRoleById(UUID roleId) {

        log.info(
                "Fetching role by ID: roleId={}",
                roleId
        );

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> {

                    log.warn(
                            "Role not found: roleId={}",
                            roleId
                    );

                    return new IllegalArgumentException(
                            "Role not found with ID: " + roleId
                    );
                });

        log.debug(
                "Role found: roleId={}, roleName={}",
                role.getRoleId(),
                role.getRoleName()
        );

        return roleMapper.toResponse(role);
    }

    @Override
    public RoleResponseDTO updateRole(
            UUID roleId,
            UpdateRoleRequestDTO request
    ) {

        log.info(
                "Role update requested: roleId={}, roleName={}",
                roleId,
                request.getRoleName()
        );

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> {

                    log.warn(
                            "Role update failed: role not found, roleId={}",
                            roleId
                    );

                    return new IllegalArgumentException(
                            "Role not found with ID: " + roleId
                    );
                });

        if (!role.getRoleName().equals(request.getRoleName())
                && roleRepository.existsByRoleName(request.getRoleName())) {

            log.warn(
                    "Role update failed: role name already exists, roleName={}",
                    request.getRoleName()
            );

            throw new IllegalArgumentException(
                    "Role with name '" + request.getRoleName() + "' already exists"
            );
        }

        log.debug(
                "Updating role fields: roleId={}",
                roleId
        );

        roleMapper.updateEntity(role, request);

        Role updatedRole = roleRepository.save(role);

        log.info(
                "Role updated successfully: roleId={}, roleName={}",
                updatedRole.getRoleId(),
                updatedRole.getRoleName()
        );

        return roleMapper.toResponse(updatedRole);
    }

    @Override
    public void deleteRole(UUID roleId) {

        log.info(
                "Role deletion requested: roleId={}",
                roleId
        );

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> {

                    log.warn(
                            "Role deletion failed: role not found, roleId={}",
                            roleId
                    );

                    return new IllegalArgumentException(
                            "Role not found with ID: " + roleId
                    );
                });

        boolean roleAssignedToUser =
                userRoleRepository.existsByRoleId(roleId);

        if (roleAssignedToUser) {

            log.warn(
                    "Role deletion blocked: role is assigned to users, roleId={}, roleName={}",
                    roleId,
                    role.getRoleName()
            );

            throw new IllegalArgumentException(
                    "Role cannot be deleted because it is assigned to one or more users"
            );
        }

        roleRepository.delete(role);

        log.info(
                "Role deleted successfully: roleId={}, roleName={}",
                roleId,
                role.getRoleName()
        );
    }
}