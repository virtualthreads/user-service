package com.aeropelican.userservice.controller;

import com.aeropelican.userservice.dto.request.CreateRoleRequest;
import com.aeropelican.userservice.dto.response.ApiResponse;
import com.aeropelican.userservice.dto.response.RoleResponse;
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

    @PostMapping
    public ResponseEntity<ApiResponse<RoleResponse>> createRole(@Valid @RequestBody CreateRoleRequest request) {
        log.info("POST /api/v1/roles request received");
        RoleResponse response = roleService.createRole(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<RoleResponse>builder().success(true).message("Role created successfully").data(response).timestamp(LocalDateTime.now()).build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<RoleResponse>>> getAllRoles() {
        log.info("GET /api/v1/roles request received");
        return ResponseEntity.ok(ApiResponse.<List<RoleResponse>>builder().success(true).message("Roles fetched successfully").data(roleService.getAllRoles()).timestamp(LocalDateTime.now()).build());
    }

    @GetMapping("/{roleId}")
    public ResponseEntity<ApiResponse<RoleResponse>> getRoleById(@PathVariable UUID roleId) {
        log.info("GET /api/v1/roles/{} request received", roleId);
        return ResponseEntity.ok(ApiResponse.<RoleResponse>builder().success(true).message("Role fetched successfully").data(roleService.getRoleById(roleId)).timestamp(LocalDateTime.now()).build());
    }

    @PutMapping("/{roleId}")
    public ResponseEntity<ApiResponse<RoleResponse>> updateRole(@PathVariable UUID roleId,
                                                              @Valid @RequestBody CreateRoleRequest request) {
        log.info("PUT /api/v1/roles/{} request received", roleId);
        return ResponseEntity.ok(ApiResponse.<RoleResponse>builder().success(true).message("Role updated successfully").data(roleService.updateRole(roleId, request)).timestamp(LocalDateTime.now()).build());
    }

    @DeleteMapping("/{roleId}")
    public ResponseEntity<ApiResponse<Void>> deleteRole(@PathVariable UUID roleId) {
        log.info("DELETE /api/v1/roles/{} request received", roleId);
        roleService.deleteRole(roleId);
        return ResponseEntity.ok(ApiResponse.<Void>builder().success(true).message("Role deleted successfully").timestamp(LocalDateTime.now()).build());
    }
}
