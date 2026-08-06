package com.aeropelican.userservice.controller;

import com.aeropelican.userservice.dto.request.AssignRoleRequest;
import com.aeropelican.userservice.dto.response.ApiResponse;
import com.aeropelican.userservice.service.RoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users/{userId}/roles")
public class UserRoleController {

    private final RoleService roleService;

    public UserRoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> assignRole(@PathVariable String userId, @RequestBody AssignRoleRequest request) {
        roleService.assignRoleToUser(userId, request.roleId());
        return ResponseEntity.ok(ApiResponse.success("Role assigned to user successfully", null));
    }

    @DeleteMapping("/{roleId}")
    public ResponseEntity<ApiResponse<Void>> removeRole(@PathVariable String userId, @PathVariable String roleId) {
        roleService.removeRoleFromUser(userId, roleId);
        return ResponseEntity.ok(ApiResponse.success("Role removed from user successfully", null));
    }
}