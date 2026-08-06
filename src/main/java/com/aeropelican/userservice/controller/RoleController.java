package com.aeropelican.userservice.controller;

import com.aeropelican.userservice.dto.request.CreateRoleRequestDTO;
import com.aeropelican.userservice.dto.request.UpdateRoleRequestDTO;
import com.aeropelican.userservice.dto.response.APIResponse;
import com.aeropelican.userservice.dto.response.RoleResponseDTO;
import com.aeropelican.userservice.service.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
@Slf4j
public class RoleController {

    private final RoleService roleService;

    /**
     * Create a new role
     */
    @PostMapping
    public ResponseEntity<APIResponse<RoleResponseDTO>> createRole(
            @Valid @RequestBody CreateRoleRequestDTO request
    ) {

        log.info(
                "POST /api/v1/roles - Create role request received: roleName={}",
                request.getRoleName()
        );

        RoleResponseDTO response = roleService.createRole(request);

        log.info(
                "POST /api/v1/roles - Role created successfully: roleId={}",
                response.getRoleId()
        );

        APIResponse<RoleResponseDTO> apiResponse = APIResponse.<RoleResponseDTO>builder()
                .success(true)
                .message("Role created successfully")
                .data(response)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(apiResponse);
    }

    /**
     * Get all roles
     */
    @GetMapping
    public ResponseEntity<APIResponse<List<RoleResponseDTO>>> getAllRoles() {

        log.info(
                "GET /api/v1/roles - Fetch all roles request received"
        );

        List<RoleResponseDTO> roles = roleService.getAllRoles();

        log.info(
                "GET /api/v1/roles - Roles fetched successfully: count={}",
                roles.size()
        );

        APIResponse<List<RoleResponseDTO>> apiResponse =
                APIResponse.<List<RoleResponseDTO>>builder()
                        .success(true)
                        .message("Roles fetched successfully")
                        .data(roles)
                        .timestamp(LocalDateTime.now())
                        .build();

        return ResponseEntity.ok(apiResponse);
    }

    /**
     * Get role by ID
     */
    @GetMapping("/{roleId}")
    public ResponseEntity<APIResponse<RoleResponseDTO>> getRoleById(
            @PathVariable UUID roleId
    ) {

        log.info(
                "GET /api/v1/roles/{} - Fetch role request received",
                roleId
        );

        RoleResponseDTO response = roleService.getRoleById(roleId);

        log.info(
                "GET /api/v1/roles/{} - Role fetched successfully",
                roleId
        );

        APIResponse<RoleResponseDTO> apiResponse =
                APIResponse.<RoleResponseDTO>builder()
                        .success(true)
                        .message("Role fetched successfully")
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build();

        return ResponseEntity.ok(apiResponse);
    }

    /**
     * Update role
     */
    @PutMapping("/{roleId}")
    public ResponseEntity<APIResponse<RoleResponseDTO>> updateRole(
            @PathVariable UUID roleId,
            @Valid @RequestBody UpdateRoleRequestDTO request
    ) {

        log.info(
                "PUT /api/v1/roles/{} - Update role request received",
                roleId
        );

        log.debug(
                "PUT /api/v1/roles/{} - Requested roleName={}",
                roleId,
                request.getRoleName()
        );

        RoleResponseDTO response =
                roleService.updateRole(roleId, request);

        log.info(
                "PUT /api/v1/roles/{} - Role updated successfully",
                roleId
        );

        APIResponse<RoleResponseDTO> apiResponse =
                APIResponse.<RoleResponseDTO>builder()
                        .success(true)
                        .message("Role updated successfully")
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build();

        return ResponseEntity.ok(apiResponse);
    }

    /**
     * Delete role
     */
    @DeleteMapping("/{roleId}")
    public ResponseEntity<APIResponse<Void>> deleteRole(
            @PathVariable UUID roleId
    ) {

        log.info(
                "DELETE /api/v1/roles/{} - Delete role request received",
                roleId
        );

        roleService.deleteRole(roleId);

        log.info(
                "DELETE /api/v1/roles/{} - Role deleted successfully",
                roleId
        );

        APIResponse<Void> apiResponse =
                APIResponse.<Void>builder()
                        .success(true)
                        .message("Role deleted successfully")
                        .data(null)
                        .timestamp(LocalDateTime.now())
                        .build();

        return ResponseEntity.ok(apiResponse);
    }
}