package com.aeropelican.userservice.controller;

import com.aeropelican.userservice.dto.request.UserRoleCreateRequestDTO;
import com.aeropelican.userservice.dto.request.UserRoleUpdateRequestDTO;
import com.aeropelican.userservice.dto.response.ApiResponse;
import com.aeropelican.userservice.dto.response.UserRoleResponse;
import com.aeropelican.userservice.service.UserRoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/user-roles")
@RequiredArgsConstructor
public class UserRoleController {

    private final UserRoleService userRoleService;

    @PostMapping
    public ResponseEntity<ApiResponse> assignRole(
            @Valid @RequestBody UserRoleCreateRequestDTO request) {

        log.info("Received request to assign role");

        UserRoleResponse response = userRoleService.assignRole(request);

        log.info("Role assigned successfully with id: {}", response.userRoleId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(
                        true,
                        "Role assigned successfully",
                        response
                ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getUserRoleById(
            @PathVariable String id) {

        log.info("Fetching UserRole with id: {}", id);

        UserRoleResponse response = userRoleService.getUserRoleById(id);

        log.info("UserRole fetched successfully");

        return ResponseEntity.ok(
                new ApiResponse(
                        true,
                        "UserRole fetched successfully",
                        response
                ));
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllUserRoles(

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "5") int size) {

        log.info("Fetching UserRoles - Page: {}, Size: {}", page, size);

        Page<UserRoleResponse> response =
                userRoleService.getAllUserRoles(page, size);

        log.info("Fetched {} user roles", response.getNumberOfElements());

        return ResponseEntity.ok(
                new ApiResponse(
                        true,
                        "UserRoles fetched successfully",
                        response
                ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateUserRole(
            @PathVariable String id,
            @Valid @RequestBody UserRoleUpdateRequestDTO request) {

        log.info("Updating UserRole with id: {}", id);

        UserRoleResponse response =
                userRoleService.updateUserRole(id, request);

        log.info("UserRole updated successfully");

        return ResponseEntity.ok(
                new ApiResponse(
                        true,
                        "UserRole updated successfully",
                        response
                ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteUserRole(
            @PathVariable String id) {

        log.info("Deleting UserRole with id: {}", id);

        userRoleService.deleteUserRole(id);

        log.info("UserRole deleted successfully");

        return ResponseEntity.ok(
                new ApiResponse(
                        true,
                        "UserRole deleted successfully",
                        null
                ));
    }
}