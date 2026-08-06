package com.aeropelican.userservice.service;
import com.aeropelican.userservice.dto.request.RoleCreateRequestDTO;
import com.aeropelican.userservice.dto.response.RoleResponseDTO;
import com.aeropelican.userservice.entity.Role;
import com.aeropelican.userservice.exceptions.DuplicateResourceException;
import com.aeropelican.userservice.exceptions.ResourceNotFoundException;
import com.aeropelican.userservice.exceptions.RoleAssignedException;
import com.aeropelican.userservice.mapper.RoleMapper;
import com.aeropelican.userservice.repository.UserRepository;
import com.aeropelican.userservice.repository.RoleRepository;
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
    public class RoleService {

        private final RoleRepository roleRepository;
        private final UserRoleRepository userRoleRepository;

        // Create Role
        public RoleResponseDTO createRole(RoleCreateRequestDTO request) {

            log.info("Creating role with name: {}", request.getRoleName());

            if (roleRepository.existsByRoleNameIgnoreCase(request.getRoleName())) {
                log.warn("Role already exists with name: {}", request.getRoleName());
                throw new DuplicateResourceException("Role already exists.");
            }

            Role role = RoleMapper.toEntity(request);

            Role savedRole = roleRepository.save(role);

            log.info("Role created successfully with id: {}", savedRole.getRoleId());

            return RoleMapper.toResponseDTO(savedRole);
        }
        // Get All Roles
        public List<RoleResponseDTO> getAllRoles() {
            log.info("Fetching all roles");
            List<RoleResponseDTO> roles = roleRepository.findAll()
                    .stream()
                    .map(RoleMapper::toResponseDTO)
                    .collect(Collectors.toList());

            log.info("Fetched {} roles", roles.size());
            return roles;
        }
        // Get Role By Id
        public RoleResponseDTO getRole(UUID roleId) {

            log.info("Fetching role with id: {}", roleId);

            Role role = roleRepository.findById(roleId)
                    .orElseThrow(() -> {
                        log.error("Role not found with id: {}", roleId);
                        return new ResourceNotFoundException("Role not found");
                    });

            log.info("Role fetched successfully");

            return RoleMapper.toResponseDTO(role);
        }


        // Update Role
        public RoleResponseDTO updateRole(UUID roleId,
                                          RoleCreateRequestDTO request) {

            log.info("Updating role with id: {}", roleId);

            Role role = roleRepository.findById(roleId)
                    .orElseThrow(() -> {
                        log.error("Role not found with id: {}", roleId);
                        return new ResourceNotFoundException("Role not found");
                    });

            roleRepository.findByRoleNameIgnoreCase(request.getRoleName())
                    .ifPresent(existing -> {
                        if (!existing.getRoleId().equals(roleId)) {
                            log.warn("Duplicate role name found: {}", request.getRoleName());
                            throw new DuplicateResourceException("Role name already exists.");
                        }
                    });

            RoleMapper.updateEntity(role, request);

            Role updated = roleRepository.save(role);

            log.info("Role updated successfully with id: {}", updated.getRoleId());

            return RoleMapper.toResponseDTO(updated);
        }
        // Delete Role
        public void deleteRole(UUID roleId) {

            log.info("Deleting role with id: {}", roleId);

            Role role = roleRepository.findById(roleId)
                    .orElseThrow(() -> {
                        log.error("Role not found with id: {}", roleId);
                        return new ResourceNotFoundException("Role not found");
                    });
            if (userRoleRepository.existsByRoleId(roleId)) {

                log.warn("Cannot delete role {} because it is assigned to one or more users.", roleId);

                throw new RoleAssignedException(
                        "Role is assigned to users.");
            }
            roleRepository.delete(role);

            log.info("Role deleted successfully with id: {}", roleId);
        }

}
