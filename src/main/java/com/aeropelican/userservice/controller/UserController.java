package com.aeropelican.userservice.controller;
import com.aeropelican.userservice.dto.request.UserCreateRequestDTO;
import com.aeropelican.userservice.dto.request.UserUpdateRequestDTO;
import com.aeropelican.userservice.dto.response.ApiResponse;
import com.aeropelican.userservice.dto.response.UserResponseDTO;
import com.aeropelican.userservice.entity.User;
import com.aeropelican.userservice.repository.UserRepository;
import com.aeropelican.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private UserRepository userRepository;

    // Get User By Id
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> getUser(
            @PathVariable UUID userId) {
        UserResponseDTO user = userService.getUser(userId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<UserResponseDTO>builder()
                        .success(true)
                        .message("User found successfully")
                        .data(user)
                        .build());
    }

    // Create User
    @PostMapping
    public ResponseEntity<ApiResponse<UserResponseDTO>> registerUser(
            @RequestBody UserCreateRequestDTO requestDTO) {

        UserResponseDTO user = userService.registerUser(requestDTO);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<UserResponseDTO>builder()
                        .success(true)
                        .message("User created successfully")
                        .data(user)
                        .build());
    }

    //To Update User
    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> updateUser(
            @PathVariable UUID userId,
            @RequestBody UserUpdateRequestDTO request) {

        UserResponseDTO user = userService.updateUser(userId, request);

        ApiResponse<UserResponseDTO> response = ApiResponse.<UserResponseDTO>builder()
                .success(true)
                .message("User updated successfully")
                .data(user)
                .build();

        return ResponseEntity.ok(response);
    }
    //To delete User
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> deleteUser(
            @PathVariable UUID userId) {

        UserResponseDTO user = userService.deleteUser(userId);

        ApiResponse<UserResponseDTO> response = ApiResponse.<UserResponseDTO>builder()
                .success(true)
                .message("User deleted successfully")
                .data(user)
                .build();

        return ResponseEntity.ok(response);
    }
}
