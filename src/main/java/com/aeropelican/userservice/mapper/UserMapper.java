package com.aeropelican.userservice.mapper;

import com.aeropelican.userservice.dto.request.CreateUserRequestDTO;
import com.aeropelican.userservice.dto.response.UserResponseDTO;
import com.aeropelican.userservice.entity.User;

public final class UserMapper {

    private UserMapper() {
        // Utility class
    }

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