package com.aeropelican.userservice.controller;

import com.aeropelican.userservice.dto.request.AssignRoleRequest;
import com.aeropelican.userservice.dto.response.ApiResponse;
import com.aeropelican.userservice.dto.response.RoleResponse;
import com.aeropelican.userservice.service.UserRoleService;
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
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserRoleController {

    private final UserRoleService userRoleService;

    @PostMapping("/{userId}/roles")
    public ResponseEntity<ApiResponse<RoleResponse>> assignRole(@PathVariable UUID userId,
                                                               @Valid @RequestBody AssignRoleRequest request) {
        log.info("POST /api/v1/users/{}/roles request received", userId);
        RoleResponse response = userRoleService.assignRole(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<RoleResponse>builder().success(true).message("Role assigned successfully").data(response).timestamp(LocalDateTime.now()).build());
    }

    @GetMapping("/{userId}/roles")
    public ResponseEntity<ApiResponse<List<RoleResponse>>> getUserRoles(@PathVariable UUID userId) {
        log.info("GET /api/v1/users/{}/roles request received", userId);
        return ResponseEntity.ok(ApiResponse.<List<RoleResponse>>builder().success(true).message("User roles fetched successfully").data(userRoleService.getUserRoles(userId)).timestamp(LocalDateTime.now()).build());
    }

    @DeleteMapping("/{userId}/roles/{roleId}")
    public ResponseEntity<ApiResponse<Void>> removeRole(@PathVariable UUID userId, @PathVariable UUID roleId) {
        log.info("DELETE /api/v1/users/{}/roles/{} request received", userId, roleId);
        userRoleService.removeRole(userId, roleId);
        return ResponseEntity.ok(ApiResponse.<Void>builder().success(true).message("Role removed successfully").timestamp(LocalDateTime.now()).build());
    }
}
