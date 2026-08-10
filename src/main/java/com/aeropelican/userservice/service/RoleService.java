package com.aeropelican.userservice.service;

import com.aeropelican.userservice.dto.request.RoleCreateRequestDTO;
import com.aeropelican.userservice.dto.request.RoleUpdateRequestDTO;
import com.aeropelican.userservice.dto.response.RoleResponseDTO;
import com.aeropelican.userservice.entity.Role;
import com.aeropelican.userservice.exceptions.RoleNotFoundException;
import com.aeropelican.userservice.mapper.RoleMapper;
import com.aeropelican.userservice.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleService {

    private final RoleRepository repository;
    private final RoleMapper mapper;

    // Create Role
    public RoleResponseDTO create(RoleCreateRequestDTO dto) {

        log.info("Received request to create role: {}", dto.getRoleName());

        if (repository.existsByRoleName(dto.getRoleName())) {
            log.warn("Role already exists with name: {}", dto.getRoleName());
            throw new RuntimeException("Role already exists with name: " + dto.getRoleName());
        }

        Role role = mapper.toEntity(dto);

        log.debug("Mapped Role entity: {}", role);

        Role savedRole = repository.save(role);

        log.info("Role created successfully with ID: {}", savedRole.getRoleId());

        return mapper.toDTO(savedRole);
    }

    // Get All Roles
    public List<RoleResponseDTO> getAll() {

        log.info("Fetching all roles");

        List<Role> roles = repository.findAll();

        log.debug("Number of roles found: {}", roles.size());

        return roles.stream()
                .map(mapper::toDTO)
                .toList();
    }

    // Get Role By ID
    public RoleResponseDTO getById(UUID id) {

        log.info("Fetching role with ID: {}", id);

        Role role = repository.findById(id)
                .orElseThrow(() -> {
                    log.error("Role not found with ID: {}", id);
                    return new RoleNotFoundException("Role not found with ID: " + id);
                });

        log.info("Role fetched successfully with ID: {}", id);
        log.debug("Role Details: {}", role);

        return mapper.toDTO(role);
    }

    // Update Role
    public RoleResponseDTO update(UUID id, RoleUpdateRequestDTO dto) {

        log.info("Received request to update role with ID: {}", id);

        Role role = repository.findById(id)
                .orElseThrow(() -> {
                    log.error("Role not found with ID: {}", id);
                    return new RoleNotFoundException("Role not found with ID: " + id);
                });

        if (!role.getRoleName().equals(dto.getRoleName())
                && repository.existsByRoleName(dto.getRoleName())) {

            log.warn("Duplicate role name found: {}", dto.getRoleName());

            throw new RuntimeException("Role already exists with name: " + dto.getRoleName());
        }

        log.debug("Existing Role: {}", role);

        role.setRoleName(dto.getRoleName());
        role.setDescription(dto.getDescription());

        Role updatedRole = repository.save(role);

        log.info("Role updated successfully with ID: {}", updatedRole.getRoleId());
        log.debug("Updated Role: {}", updatedRole);

        return mapper.toDTO(updatedRole);
    }

    // Delete Role
    public void delete(UUID id) {

        log.info("Received request to delete role with ID: {}", id);

        Role role = repository.findById(id)
                .orElseThrow(() -> {
                    log.error("Role not found with ID: {}", id);
                    return new RoleNotFoundException("Role not found with ID: " + id);
                });

        repository.delete(role);

        log.info("Role deleted successfully with ID: {}", id);
    }
}