package com.aeropelican.userservice.controller;

import com.aeropelican.userservice.dto.response.ApiResponse;
import com.aeropelican.userservice.dto.request.CreateUserRequest;
import com.aeropelican.userservice.dto.response.AuthUser;
import com.aeropelican.userservice.dto.response.PageResponse;
import com.aeropelican.userservice.dto.request.UpdateUserRequest;
import com.aeropelican.userservice.dto.request.UpdateUserStatusRequest;
import com.aeropelican.userservice.dto.response.UserResponse;
import com.aeropelican.userservice.dto.request.UserSearchRequest;
import com.aeropelican.userservice.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> registerUser(@Valid @RequestBody CreateUserRequest request) {
        logger.info("REST request to register user with email: {}", request.email());
        UserResponse createdUser = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(createdUser, "User registered successfully"));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable UUID userId) {
        logger.info("REST request to get user by ID: {}", userId);
        UserResponse user = userService.getUserById(userId);
        return ResponseEntity.ok(ApiResponse.success(user, "User retrieved successfully"));
    }

    @PostMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<UserResponse>>> searchUsers(@RequestBody UserSearchRequest request) {
        logger.info("REST request to search users with keyword: '{}', page: {}, size: {}",
                request.keyword(), request.page(), request.size());
        PageResponse<UserResponse> pageResponse = userService.searchUsers(request);
        return ResponseEntity.ok(ApiResponse.success(pageResponse, "Users search executed successfully"));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(
            @PathVariable UUID userId,
            @Valid @RequestBody UpdateUserRequest request) {
        logger.info("REST request to update user profile for ID: {}", userId);
        UserResponse updatedUser = userService.updateUser(userId, request);
        return ResponseEntity.ok(ApiResponse.success(updatedUser, "User updated successfully"));
    }

    @PatchMapping("/{userId}/status")
    public ResponseEntity<ApiResponse<UserResponse>> updateUserStatus(
            @PathVariable UUID userId,
            @Valid @RequestBody UpdateUserStatusRequest request) {
        logger.info("REST request to update status for user ID: {} to {}", userId, request.status());
        UserResponse updatedUser = userService.updateUserStatus(userId, request);
        return ResponseEntity.ok(ApiResponse.success(updatedUser, "User status updated successfully"));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable UUID userId) {
        logger.info("REST request to soft-delete user ID: {}", userId);
        userService.deleteUser(userId);
        return ResponseEntity.ok(ApiResponse.success(null, "User deleted successfully"));
    }

    @GetMapping("/email")
    public ResponseEntity<ApiResponse<AuthUser>> findUserByEmail(@RequestParam("email") String email) {
        return ResponseEntity.ok(ApiResponse.success(userService.findByEmail(email), "Success"));
    }
}
