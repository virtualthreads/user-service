package com.aeropelican.userservice.service.impl;
import com.aeropelican.userservice.dto.request.RoleCreateRequestDTO;
import com.aeropelican.userservice.dto.request.RoleUpdateRequestDTO;
import com.aeropelican.userservice.dto.response.RoleResponse;
import com.aeropelican.userservice.entity.Role;
import com.aeropelican.userservice.exception.RoleNotFoundException;
import com.aeropelican.userservice.mapper.RoleMapper;
import com.aeropelican.userservice.repository.RoleRepository;
import com.aeropelican.userservice.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {


    private final RoleRepository roleRepository;

    @Override
    public RoleResponse createRole(RoleCreateRequestDTO request) {

        log.info("Creating role with name: {}", request.roleName());

        if (roleRepository.existsByRoleName(request.roleName())) {

            log.error("Role already exists: {}", request.roleName());

            throw new RoleNotFoundException("Role already exists.");
        }

        Role role = RoleMapper.toEntity(request);

        roleRepository.save(role);

        log.info("Role created successfully with id: {}", role.getRoleId());

        return RoleMapper.toResponse(role);
    }
    @Override
    public RoleResponse getRoleById(String id) {

        log.info("Fetching role with id: {}", id);

        Role role = roleRepository.findById(id)
                .orElseThrow(() -> {

                    log.error("Role not found with id: {}", id);

                    return new RoleNotFoundException(
                            "Role not found with id: " + id);
                });

        log.info("Role fetched successfully.");

        return RoleMapper.toResponse(role);
    }
    @Override
    public Page<RoleResponse> getAllRoles(int page, int size) {

        log.info("Fetching roles. Page: {}, Size: {}", page, size);

        Pageable pageable = PageRequest.of(page, size);

        Page<RoleResponse> roles = roleRepository.findAll(pageable)
                .map(RoleMapper::toResponse);

        log.info("Fetched {} roles", roles.getNumberOfElements());

        return roles;
    } @Override
    public RoleResponse updateRole(String id,
                                   RoleUpdateRequestDTO request) {

        log.info("Updating role with id: {}", id);

        Role role = roleRepository.findById(id)
                .orElseThrow(() -> {

                   log.error("Role not found with id: {}", id);

                    return new RoleNotFoundException(
                            "Role not found with id: " + id);
                });

        RoleMapper.updateEntity(role, request);

        roleRepository.save(role);

        log.info("Role updated successfully with id: {}", role.getRoleId());
        return RoleMapper.toResponse(role);
    }

    @Override
    public void deleteRole(String id) {

        log.info("Deleting role with id: {}", id);

        Role role = roleRepository.findById(id)
                .orElseThrow(() -> {

                    log.error("Role not found with id: {}", id);

                    return new RoleNotFoundException(
                            "Role not found with id: " + id);
                });

        roleRepository.delete(role);

        log.info("Role deleted successfully with id: {}", id);    }

}