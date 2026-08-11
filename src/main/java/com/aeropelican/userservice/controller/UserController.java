package com.aeropelican.userservice.controller;

import com.aeropelican.userservice.dto.request.UserCreateRequestDTO;
import com.aeropelican.userservice.dto.request.UserUpdateRequestDTO;
import com.aeropelican.userservice.dto.response.UserResponse;
import com.aeropelican.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> createUser(
            @RequestBody UserCreateRequestDTO request) {

        log.info("Received request to create user");

        UserResponse response = userService.createUser(request);

        log.info("User created successfully with id: {}", response.userId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(
            @PathVariable String id) {

        log.info("Fetching user with id: {}", id);

        UserResponse response = userService.getUserById(id);

        log.info("User fetched successfully");

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<UserResponse>> getAllUsers(

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "5") int size) {

        log.info("Fetching users - Page: {}, Size: {}", page, size);

        Page<UserResponse> users = userService.getAllUsers(page, size);

        log.info("Users fetched successfully");

        return ResponseEntity.ok(users);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable String id,
            @RequestBody UserUpdateRequestDTO request) {

        log.info("Updating user with id: {}", id);

        UserResponse response = userService.updateUser(id, request);

        log.info("User updated successfully");

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(
            @PathVariable String id) {

        log.info("Deleting user with id: {}", id);

        userService.deleteUser(id);

        log.info("User deleted successfully");

        return ResponseEntity.ok("User deleted successfully");
    }
}