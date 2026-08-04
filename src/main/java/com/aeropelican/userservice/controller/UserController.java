package com.aeropelican.userservice.controller;

import com.aeropelican.userservice.dto.request.UserCreateRequestDTO;
import com.aeropelican.userservice.dto.request.UserUpdateRequestDTO;
import com.aeropelican.userservice.dto.response.UserResponseDTO;
import com.aeropelican.userservice.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(
            @RequestBody UserCreateRequestDTO requestDTO) {

        return new ResponseEntity<>(
                userService.createUser(requestDTO),
                HttpStatus.CREATED
        );
    }


    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {

        return ResponseEntity.ok(
                userService.getAllUsers()
        );
    }


    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(
            @PathVariable String id) {

        return ResponseEntity.ok(
                userService.getUserById(id)
        );
    }


    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable String id,
            @RequestBody UserUpdateRequestDTO requestDTO) {

        return ResponseEntity.ok(
                userService.updateUser(id, requestDTO)
        );
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(
            @PathVariable String id) {

        userService.deleteUser(id);

        return ResponseEntity.ok(
                "User deleted successfully"
        );
    }
}