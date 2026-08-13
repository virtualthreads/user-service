package com.aeropelican.userservice.controller;

import com.aeropelican.userservice.dto.ApiResponse;
import com.aeropelican.userservice.dto.AssignRoleRequest;
import com.aeropelican.userservice.dto.RoleResponse;
import com.aeropelican.userservice.service.UserRoleService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users/{userId}/roles")
public class UserRoleController {

    private static final Logger logger = LoggerFactory.getLogger(UserRoleController.class);

    private final UserRoleService userRoleService;

    public UserRoleController(UserRoleService userRoleService) {
        this.userRoleService = userRoleService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<RoleResponse>> assignRole(
            @PathVariable UUID userId,
            @Valid @RequestBody AssignRoleRequest request) {
        logger.info("REST request to assign role ID {} to user ID {}", request.roleId(), userId);
        RoleResponse assignedRole = userRoleService.assignRole(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(assignedRole, "Role assigned successfully"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<RoleResponse>>> getUserRoles(@PathVariable UUID userId) {
        logger.info("REST request to fetch roles for user ID {}", userId);
        List<RoleResponse> roles = userRoleService.getUserRoles(userId);
        return ResponseEntity.ok(ApiResponse.success(roles, "User roles retrieved successfully"));
    }

    @DeleteMapping("/{roleId}")
    public ResponseEntity<ApiResponse<Void>> removeRole(
            @PathVariable UUID userId,
            @PathVariable UUID roleId) {
        logger.info("REST request to remove role ID {} from user ID {}", roleId, userId);
        userRoleService.removeRole(userId, roleId);
        return ResponseEntity.ok(ApiResponse.success(null, "Role removed successfully"));
    }
}
