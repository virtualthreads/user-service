package com.aeropelican.userservice.controller;

import com.aeropelican.userservice.dto.request.UserRoleCreateRequestDTO;
import com.aeropelican.userservice.dto.request.UserRoleUpdateRequestDTO;
import com.aeropelican.userservice.dto.response.UserRoleResponseDTO;
import com.aeropelican.userservice.service.UserRoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/user-roles")
@RequiredArgsConstructor
public class UserRoleController {

    private final UserRoleService userRoleService;

    @PostMapping
    public ResponseEntity<UserRoleResponseDTO> createUserRole(
            @Valid @RequestBody UserRoleCreateRequestDTO requestDTO) {

        return new ResponseEntity<>(
                userRoleService.createUserRole(requestDTO),
                HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<UserRoleResponseDTO>> getAllUserRoles() {
        return ResponseEntity.ok(userRoleService.getAllUserRoles());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserRoleResponseDTO> getUserRoleById(@PathVariable UUID id) {
        return ResponseEntity.ok(userRoleService.getUserRoleById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserRoleResponseDTO> updateUserRole(
            @PathVariable UUID id,
            @Valid @RequestBody UserRoleUpdateRequestDTO requestDTO) {

        return ResponseEntity.ok(userRoleService.updateUserRole(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserRole(@PathVariable UUID id) {
        userRoleService.deleteUserRole(id);
        return ResponseEntity.noContent().build();
    }
}