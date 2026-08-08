package com.aeropelican.userservice.service;

import com.aeropelican.userservice.dto.request.CreateUserRequestDTO;
import com.aeropelican.userservice.dto.request.UpdateUserRequestDTO;
import com.aeropelican.userservice.dto.request.UserSearchRequestDTO;
import com.aeropelican.userservice.dto.response.PageResponse;
import com.aeropelican.userservice.dto.response.UserResponseDTO;

import java.util.UUID;

public interface UserService {

    // =========================================================
    // CREATE USER
    // =========================================================

    UserResponseDTO createUser(
            CreateUserRequestDTO request
    );

    // =========================================================
    // GET USER
    // =========================================================

    UserResponseDTO getUser(
            UUID userId
    );

    // =========================================================
    // SEARCH USERS
    // =========================================================

    PageResponse<UserResponseDTO> searchUsers(
            UserSearchRequestDTO request
    );

    // =========================================================
    // UPDATE USER
    // =========================================================

    UserResponseDTO updateUser(
            UUID userId,
            UpdateUserRequestDTO request
    );

    // =========================================================
    // UPDATE USER STATUS
    // =========================================================

    UserResponseDTO updateUserStatus(
            UUID userId,
            String status
    );

    // =========================================================
    // DELETE USER
    // =========================================================

    void deleteUser(
            UUID userId
    );
}