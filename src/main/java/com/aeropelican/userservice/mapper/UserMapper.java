package com.aeropelican.userservice.mapper;

import com.aeropelican.userservice.dto.request.UserCreateRequestDTO;
import com.aeropelican.userservice.dto.request.UserUpdateRequestDTO;
import com.aeropelican.userservice.dto.response.UserResponse;
import com.aeropelican.userservice.entity.User;
import com.aeropelican.userservice.enums.UserStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public class UserMapper {

    private UserMapper() {
    }

    public static User toEntity(UserCreateRequestDTO request) {

        return User.builder()
                .userId(UUID.randomUUID().toString())
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .phoneNumber(request.phoneNumber())
                .passwordHash(request.passwordHash())
                .gender(request.gender())
                .dateOfBirth(request.dateOfBirth())
                .emailVerified(false)
                .phoneVerified(false)
                .status(UserStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static UserResponse toResponse(User user) {

        return new UserResponse(
                user.getUserId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getGender(),
                user.getDateOfBirth(),
                user.getEmailVerified(),
                user.getPhoneVerified(),
                user.getStatus()
        );
    }

    public static void updateEntity(User user,
                                    UserUpdateRequestDTO request) {

        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setPhoneNumber(request.phoneNumber());
        user.setDateOfBirth(request.dateOfBirth());
        user.setStatus(request.status());
        user.setUpdatedAt(LocalDateTime.now());

    }

}