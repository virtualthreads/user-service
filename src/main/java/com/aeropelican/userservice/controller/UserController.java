package com.aeropelican.userservice.controller;

import com.aeropelican.userservice.dto.request.CreateUserRequest;
import com.aeropelican.userservice.dto.request.UpdateUserRequest;
import com.aeropelican.userservice.dto.request.UpdateUserStatusRequest;
import com.aeropelican.userservice.dto.request.UserSearchRequest;
import com.aeropelican.userservice.dto.response.ApiResponse;
import com.aeropelican.userservice.dto.response.PageResponse;
import com.aeropelican.userservice.dto.response.UserResponse;
import com.aeropelican.userservice.service.UserService;
import com.aeropelican.commonsservice.user.dto.response.UserAuthResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> createUser(@Valid @RequestBody CreateUserRequest request) {
        log.info("POST /api/v1/users request received");
        UserResponse response = userService.createUser(request);
        ApiResponse<UserResponse> apiResponse = ApiResponse.<UserResponse>builder()
                .success(true)
                .message("User registered successfully")
                .data(response)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable UUID userId) {
        log.info("GET /api/v1/users/{} request received", userId);
        UserResponse response = userService.getUserById(userId);
        ApiResponse<UserResponse> apiResponse = ApiResponse.<UserResponse>builder()
                .success(true)
                .message("User fetched successfully")
                .data(response)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<UserResponse>>> searchUsers(@RequestBody UserSearchRequest request) {
        log.info("POST /api/v1/users/search request received");
        PageResponse<UserResponse> response = userService.searchUsers(request);
        ApiResponse<PageResponse<UserResponse>> apiResponse = ApiResponse.<PageResponse<UserResponse>>builder()
                .success(true)
                .message("Users fetched successfully")
                .data(response)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(@PathVariable UUID userId,
                                                              @Valid @RequestBody UpdateUserRequest request) {
        log.info("PUT /api/v1/users/{} request received", userId);
        UserResponse response = userService.updateUser(userId, request);
        ApiResponse<UserResponse> apiResponse = ApiResponse.<UserResponse>builder()
                .success(true)
                .message("User updated successfully")
                .data(response)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PatchMapping("/{userId}/status")
    public ResponseEntity<ApiResponse<UserResponse>> updateUserStatus(@PathVariable UUID userId,
                                                                     @Valid @RequestBody UpdateUserStatusRequest request) {
        log.info("PATCH /api/v1/users/{}/status request received", userId);
        UserResponse response = userService.updateUserStatus(userId, request);
        ApiResponse<UserResponse> apiResponse = ApiResponse.<UserResponse>builder()
                .success(true)
                .message("User status updated successfully")
                .data(response)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable UUID userId) {
        log.info("DELETE /api/v1/users/{} request received", userId);
        userService.deleteUser(userId);
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .success(true)
                .message("User deleted successfully")
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/auth/email/{email}")
    public ResponseEntity<ApiResponse<UserAuthResponse>> getUserByEmailForAuth(@PathVariable String email) {
        log.info("GET /api/v1/users/auth/email/{} request received", email);
        UserAuthResponse response = (UserAuthResponse) userService.getUserByEmailForAuth(email);
        ApiResponse<UserAuthResponse> apiResponse = ApiResponse.<UserAuthResponse>builder()
                .success(true)
                .message("User authentication details fetched successfully")
                .data(response)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}
