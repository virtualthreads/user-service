package com.aeropelican.userservice.service;
import com.aeropelican.userservice.dto.request.RoleCreateRequestDTO;
import com.aeropelican.userservice.dto.response.RoleResponseDTO;
import com.aeropelican.userservice.entity.Role;
import com.aeropelican.userservice.exceptions.*;
import com.aeropelican.userservice.mapper.RoleMapper;
import com.aeropelican.userservice.repository.RoleRepository;
import com.aeropelican.userservice.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
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


    public RoleResponseDTO createRole(RoleCreateRequestDTO request) {

        log.info("Creating role with name: {}", request.getRoleName());

        if (roleRepository.existsByRoleNameIgnoreCase(request.getRoleName())) {
            log.warn("Role already exists with name: {}", request.getRoleName());
            throw new DuplicateResource("Role already exists.");
        }

        Role role = RoleMapper.toEntity(request);

        Role savedRole = roleRepository.save(role);

        log.info("Role created successfully with id: {}", savedRole.getRoleId());

        return RoleMapper.toResponseDTO(savedRole);
    }

    @Cacheable(value="Create Role",key = "#roleId")
    public List<RoleResponseDTO> getAllRoles() {
        log.info("Fetching all roles");
        List<RoleResponseDTO> roles = roleRepository.findAll()
                .stream()
                .map(RoleMapper::toResponseDTO)
                .collect(Collectors.toList());

        log.info("Fetched {} roles", roles.size());
        return roles;
    }

    @Cacheable(value="Create Role",key = "#roleId")
    public RoleResponseDTO getRole(UUID roleId) {

        log.info("Fetching role with id: {}", roleId);

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> {
                    log.error("Role not found with id: {}", roleId);
                    return new ResourceNotFound("Role not found");
                });

        log.info("Role fetched successfully");

        return RoleMapper.toResponseDTO(role);
    }


    public RoleResponseDTO updateRole(UUID roleId,
                                      RoleCreateRequestDTO request) {

        log.info("Updating role with id: {}", roleId);

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> {
                    log.error("Role not found with id: {}", roleId);
                    return new ResourceNotFound("Role not found");
                });

        roleRepository.findByRoleNameIgnoreCase(request.getRoleName())
                .ifPresent(existing -> {
                    if (!existing.getRoleId().equals(roleId)) {
                        log.warn("Duplicate role name found: {}", request.getRoleName());
                        throw new DuplicateResource("Role name already exists.");
                    }
                });

        RoleMapper.updateEntity(role, request);

        Role updated = roleRepository.save(role);

        log.info("Role updated successfully with id: {}", updated.getRoleId());

        return RoleMapper.toResponseDTO(updated);
    }

    public void deleteRole(UUID roleId) {

        log.info("Deleting role with id: {}", roleId);

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> {
                    log.error("Role not found with id: {}", roleId);
                    return new ResourceNotFound("Role not found");
                });
        roleRepository.delete(role);

        log.info("Role deleted successfully with id: {}", roleId);
    }

}
