package com.aeropelican.userservice.controller;

import com.aeropelican.userservice.dto.request.AssignRoleRequestDTO;
import com.aeropelican.userservice.dto.response.APIResponse;
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

    // =========================================================
    // ASSIGN ROLE
    // =========================================================

    @PostMapping
    public ResponseEntity<APIResponse<UserRoleResponseDTO>> assignRole(
            @PathVariable UUID userId,
            @Valid @RequestBody AssignRoleRequestDTO request) {

        UserRoleResponseDTO response =
                userRoleService.assignRole(
                        userId,
                        request
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        APIResponse.success(
                                response,
                                "Role assigned successfully"
                        )
                );
    }

    // =========================================================
    // GET USER ROLES
    // =========================================================

    @GetMapping
    public ResponseEntity<
            APIResponse<List<UserRoleResponseDTO>>> getUserRoles(
            @PathVariable UUID userId) {

        List<UserRoleResponseDTO> response =
                userRoleService.getUserRoles(userId);

        return ResponseEntity.ok(
                APIResponse.success(
                        response,
                        "User roles fetched successfully"
                )
        );
    }

    // =========================================================
    // REMOVE ROLE
    // =========================================================

    @DeleteMapping("/{roleId}")
    public ResponseEntity<APIResponse<Void>> removeRole(
            @PathVariable UUID userId,
            @PathVariable UUID roleId) {

        userRoleService.removeRole(
                userId,
                roleId
        );

        return ResponseEntity.ok(
                APIResponse.success(
                        null,
                        "Role removed successfully"
                )
        );
    }
}