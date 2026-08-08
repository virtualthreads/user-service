package com.aeropelican.userservice.mapper;

import com.aeropelican.userservice.dto.request.CreateUserRequestDTO;
import com.aeropelican.userservice.dto.request.UpdateUserRequestDTO;
import com.aeropelican.userservice.dto.response.UserResponseDTO;
import com.aeropelican.userservice.entity.User;

public final class UserMapper {

    private UserMapper() {
        // Utility class
    }

    // =========================================================
    // CREATE USER
    // =========================================================

    public static User toEntity(CreateUserRequestDTO request) {

        return User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .passwordHash(request.getPassword())
                .gender(request.getGender())
                .dateOfBirth(request.getDateOfBirth())
                .build();
    }

    // =========================================================
    // UPDATE USER
    // =========================================================

    public static void updateEntity(User user,
                                    UpdateUserRequestDTO request) {

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setGender(request.getGender());
        user.setDateOfBirth(request.getDateOfBirth());
    }

    // =========================================================
    // RESPONSE DTO
    // =========================================================

    public static UserResponseDTO toResponse(User user) {

        return UserResponseDTO.builder()
                .userId(user.getUserId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .gender(user.getGender())
                .dateOfBirth(user.getDateOfBirth())
                .emailVerified(user.getEmailVerified())
                .phoneVerified(user.getPhoneVerified())
                .status(user.getStatus())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

}