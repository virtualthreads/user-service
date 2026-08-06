package com.aeropelican.userservice.service;

import com.aeropelican.userservice.dto.request.CreateUserRequestDTO;
import com.aeropelican.userservice.dto.request.UpdateUserRequestDTO;
import com.aeropelican.userservice.dto.response.UserResponseDTO;

import java.util.List;
import java.util.UUID;

public interface UserService {

    /**
     * Register a new user.
     */
    UserResponseDTO createUser(CreateUserRequestDTO request);

    /**
     * Fetch a user by UUID.
     */
    UserResponseDTO getUser(UUID userId);

    /**
     * Search users based on keyword and status.
     */
    List<UserResponseDTO> searchUsers(String keyword, String status);

    /**
     * Update user details.
     */
    UserResponseDTO updateUser(UUID userId, UpdateUserRequestDTO request);

    /**
     * Update user status.
     */
    UserResponseDTO updateUserStatus(UUID userId, String status);

    /**
     * Soft delete a user.
     */
    void deleteUser(UUID userId);
}