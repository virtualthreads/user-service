package com.aeropelican.userservice.controller;

import com.aeropelican.userservice.dto.request.CreateUserRequestDTO;
import com.aeropelican.userservice.dto.response.APIResponse;
import com.aeropelican.userservice.dto.response.UserResponseDTO;
import com.aeropelican.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<APIResponse<UserResponseDTO>> createUser(
            @Valid @RequestBody CreateUserRequestDTO request) {

        UserResponseDTO response = userService.createUser(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        APIResponse.success(
                                response,
                                "User created successfully"
                        )
                );
    }
}