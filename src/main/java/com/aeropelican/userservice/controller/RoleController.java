package com.aeropelican.userservice.controller;
import com.aeropelican.userservice.dto.request.RoleCreateRequestDTO;
import com.aeropelican.userservice.dto.response.ApiResponse;
import com.aeropelican.userservice.dto.response.RoleResponseDTO;
import com.aeropelican.userservice.service.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;
@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
@Valid
@Slf4j
public class RoleController {
    private final RoleService roleService;

    // Create Role
    @PostMapping
    public ResponseEntity<ApiResponse<RoleResponseDTO>> createRole(
            @Valid @RequestBody RoleCreateRequestDTO request) {

        log.info("Received request to create role");

        RoleResponseDTO response = roleService.createRole(request);

        log.info("Returning response for created role");

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<RoleResponseDTO>builder()
                        .success(true)
                        .message("Role created successfully")
                        .data(response)
                        .build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<RoleResponseDTO>>> getAllRoles() {

        log.info("Received request to fetch all roles");

        List<RoleResponseDTO> response = roleService.getAllRoles();

        return ResponseEntity.ok(
                ApiResponse.<List<RoleResponseDTO>>builder()
                        .success(true)
                        .message("Roles fetched successfully")
                        .data(response)
                        .build());
    }

    @GetMapping("/{roleId}")
    public ResponseEntity<ApiResponse<RoleResponseDTO>> getRole(
            @PathVariable UUID roleId) {

        log.info("Received request to fetch role {}", roleId);

        RoleResponseDTO response = roleService.getRole(roleId);

        return ResponseEntity.ok(
                ApiResponse.<RoleResponseDTO>builder()
                        .success(true)
                        .message("Role fetched successfully")
                        .data(response)
                        .build());
    }

    @PutMapping("/{roleId}")
    public ResponseEntity<ApiResponse<RoleResponseDTO>> updateRole(
            @PathVariable UUID roleId,
            @Valid @RequestBody RoleCreateRequestDTO request) {

        log.info("Received request to update role {}", roleId);

        RoleResponseDTO response = roleService.updateRole(roleId, request);

        return ResponseEntity.ok(
                ApiResponse.<RoleResponseDTO>builder()
                        .success(true)
                        .message("Role updated successfully")
                        .data(response)
                        .build());
    }

    @DeleteMapping("/{roleId}")
    public ResponseEntity<ApiResponse<Void>> deleteRole(
            @PathVariable UUID roleId) {

        log.info("Received request to delete role {}", roleId);

        roleService.deleteRole(roleId);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Role deleted successfully")
                        .build());
    }
}