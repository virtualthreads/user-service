package com.aeropelican.userservice.controller;

import com.aeropelican.userservice.dto.request.CreateUserRequestDTO;
import com.aeropelican.userservice.dto.request.UpdateUserRequestDTO;
import com.aeropelican.userservice.dto.request.UpdateUserStatusRequestDTO;
import com.aeropelican.userservice.dto.request.UserSearchRequestDTO;
import com.aeropelican.userservice.dto.response.APIResponse;
import com.aeropelican.userservice.dto.response.PageResponse;
import com.aeropelican.userservice.dto.response.UserResponseDTO;
import com.aeropelican.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    // =========================================================
    // CREATE USER
    // =========================================================

    @PostMapping
    public ResponseEntity<APIResponse<UserResponseDTO>> createUser(
            @Valid @RequestBody CreateUserRequestDTO request) {

        log.info("Received request to create user");

        UserResponseDTO response =
                userService.createUser(request);

        log.info("User created successfully");

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        APIResponse.success(
                                response,
                                "User created successfully"
                        )
                );
    }

    // =========================================================
    // GET USER BY ID
    // =========================================================

    @GetMapping("/{userId}")
    public ResponseEntity<APIResponse<UserResponseDTO>> getUser(
            @PathVariable UUID userId) {

        log.info(
                "Received request to fetch user : {}",
                userId
        );

        UserResponseDTO response =
                userService.getUser(userId);

        return ResponseEntity.ok(
                APIResponse.success(
                        response,
                        "User fetched successfully"
                )
        );
    }

    // =========================================================
    // SEARCH USERS
    // =========================================================

    @PostMapping("/search")
    public ResponseEntity<
            APIResponse<PageResponse<UserResponseDTO>>
            > searchUsers(
            @RequestBody UserSearchRequestDTO request) {

        log.info(
                "Received search request. keyword={}, status={}, gender={}",
                request.getKeyword(),
                request.getStatus(),
                request.getGender()
        );

        PageResponse<UserResponseDTO> response =
                userService.searchUsers(request);

        return ResponseEntity.ok(
                APIResponse.success(
                        response,
                        "Users fetched successfully"
                )
        );
    }

    // =========================================================
    // UPDATE USER
    // =========================================================

    @PutMapping("/{userId}")
    public ResponseEntity<APIResponse<UserResponseDTO>> updateUser(
            @PathVariable UUID userId,
            @Valid @RequestBody UpdateUserRequestDTO request) {

        log.info(
                "Received request to update user : {}",
                userId
        );

        UserResponseDTO response =
                userService.updateUser(
                        userId,
                        request
                );

        log.info("User updated successfully");

        return ResponseEntity.ok(
                APIResponse.success(
                        response,
                        "User updated successfully"
                )
        );
    }

    // =========================================================
    // UPDATE USER STATUS
    // =========================================================

    @PatchMapping("/{userId}/status")
    public ResponseEntity<APIResponse<UserResponseDTO>> updateUserStatus(
            @PathVariable UUID userId,
            @Valid @RequestBody UpdateUserStatusRequestDTO request) {

        log.info(
                "Received request to update user status : {}",
                userId
        );

        UserResponseDTO response =
                userService.updateUserStatus(
                        userId,
                        request.getStatus().name()
                );

        log.info("User status updated successfully");

        return ResponseEntity.ok(
                APIResponse.success(
                        response,
                        "User status updated successfully"
                )
        );
    }

    // =========================================================
    // DELETE USER - SOFT DELETE
    // =========================================================

    @DeleteMapping("/{userId}")
    public ResponseEntity<APIResponse<Void>> deleteUser(
            @PathVariable UUID userId) {

        log.info(
                "Received request to delete user : {}",
                userId
        );

        userService.deleteUser(userId);

        log.info("User deleted successfully");

        return ResponseEntity.ok(
                APIResponse.success(
                        null,
                        "User deleted successfully"
                )
        );
    }
}