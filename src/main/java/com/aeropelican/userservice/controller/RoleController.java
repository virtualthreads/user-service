package com.aeropelican.userservice.controller;

import com.aeropelican.userservice.dto.request.RoleCreateRequestDTO;
import com.aeropelican.userservice.dto.request.RoleUpdateRequestDTO;
import com.aeropelican.userservice.dto.response.RoleResponseDTO;
import com.aeropelican.userservice.service.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
@Slf4j
public class RoleController {

    private final RoleService roleService;

    // Create Role
    @PostMapping
    public ResponseEntity<RoleResponseDTO> createRole(
            @Valid @RequestBody RoleCreateRequestDTO requestDTO) {

        log.info("Received request to create role.");

        RoleResponseDTO response = roleService.create(requestDTO);

        log.info("Role created successfully.");

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Get All Roles
    @GetMapping
    public ResponseEntity<List<RoleResponseDTO>> getAllRoles() {

        log.info("Received request to fetch all roles.");

        List<RoleResponseDTO> roles = roleService.getAll();

        log.info("Fetched {} role(s).", roles.size());

        return ResponseEntity.ok(roles);
    }

    // Get Role By ID
    @GetMapping("/{id}")
    public ResponseEntity<RoleResponseDTO> getRoleById(
            @PathVariable UUID id) {

        log.info("Received request to fetch role with ID: {}", id);

        RoleResponseDTO response = roleService.getById(id);

        log.info("Role fetched successfully.");

        return ResponseEntity.ok(response);
    }

    // Update Role
    @PutMapping("/{id}")
    public ResponseEntity<RoleResponseDTO> updateRole(
            @PathVariable UUID id,
            @Valid @RequestBody RoleUpdateRequestDTO requestDTO) {

        log.info("Received request to update role with ID: {}", id);

        RoleResponseDTO response = roleService.update(id, requestDTO);

        log.info("Role updated successfully.");

        return ResponseEntity.ok(response);
    }

    // Delete Role
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(
            @PathVariable UUID id) {

        log.info("Received request to delete role with ID: {}", id);

        roleService.delete(id);

        log.info("Role deleted successfully.");

        return ResponseEntity.noContent().build();
    }
}