package com.aeropelican.userservice.controller;

import com.aeropelican.userservice.dto.request.AssignRoleRequestDTO;
import com.aeropelican.userservice.dto.response.ApiResponse;
import com.aeropelican.userservice.dto.response.UserRoleResponseDTO;
import com.aeropelican.userservice.service.UserRoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users/{userId}/roles")
@RequiredArgsConstructor
@Slf4j

public class UserRoleController {
    private final UserRoleService userRoleService;


    @PostMapping
    public ResponseEntity<ApiResponse<UserRoleResponseDTO>> assignRole(
            @PathVariable UUID userId,
            @Valid @RequestBody AssignRoleRequestDTO request) {

        log.info("Received request to assign role {} to user {}",
                request.getRoleId(), userId);

        UserRoleResponseDTO response =
                userRoleService.assignRole(userId, request);

        log.info("Role assigned successfully.");

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<UserRoleResponseDTO>builder()
                        .success(true)
                        .message("Role assigned successfully")
                        .data(response)
                        .build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserRoleResponseDTO>>> getUserRoles(
            @PathVariable UUID userId) {

        log.info("Received request to fetch roles of user {}", userId);

        List<UserRoleResponseDTO> response =
                userRoleService.getUserRoles(userId);

        return ResponseEntity.ok(
                ApiResponse.<List<UserRoleResponseDTO>>builder()
                        .success(true)
                        .message("User roles fetched successfully")
                        .data(response)
                        .build());
    }

    @DeleteMapping("/{roleId}")
    public ResponseEntity<ApiResponse<Void>> removeRole(
            @PathVariable UUID userId,
            @PathVariable UUID roleId) {

        log.info("Received request to remove role {} from user {}",
                roleId, userId);

        userRoleService.removeRole(userId, roleId);

        log.info("Role removed successfully.");

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Role removed successfully")
                        .build());
    }
}
