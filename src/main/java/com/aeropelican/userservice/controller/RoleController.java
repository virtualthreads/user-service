package com.aeropelican.userservice.controller;

import com.aeropelican.userservice.dto.request.RoleCreateRequestDTO;
import com.aeropelican.userservice.dto.request.RoleUpdateRequestDTO;
import com.aeropelican.userservice.dto.response.RoleResponse;
import com.aeropelican.userservice.service.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import lombok.extern.slf4j.Slf4j;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Valid
@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @PostMapping
    public ResponseEntity<RoleResponse> createRole(
            @RequestBody RoleCreateRequestDTO request) {

        log.info("Received request to create role");

        RoleResponse response = roleService.createRole(request);

        log.info("Role created successfully with id: {}", response.roleId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleResponse> getRoleById(
            @PathVariable String id) {

        log.info("Fetching role with id: {}", id);

        RoleResponse response = roleService.getRoleById(id);

        log.info("Role fetched successfully");

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<RoleResponse>> getAllRoles(

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "5") int size) {

        log.info("Fetching roles - Page: {}, Size: {}", page, size);

        return ResponseEntity.ok(
                roleService.getAllRoles(page, size)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoleResponse> updateRole(
            @PathVariable String id,
            @RequestBody RoleUpdateRequestDTO request) {

        log.info("Updating role with id: {}", id);

        RoleResponse response =
                roleService.updateRole(id, request);

        log.info("Role updated successfully");

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRole(
            @PathVariable String id) {

        log.info("Deleting role with id: {}", id);

        roleService.deleteRole(id);

        log.info("Role deleted successfully");

        return ResponseEntity.ok("Role deleted successfully");
    }
}