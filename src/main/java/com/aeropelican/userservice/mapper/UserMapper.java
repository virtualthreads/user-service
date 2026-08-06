package com.aeropelican.userservice.mapper;

import com.aeropelican.userservice.dto.Request.CreateUserRequest;
import com.aeropelican.userservice.dto.Response.UserResponse;
import com.aeropelican.userservice.entity.User;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserMapper {

    public User toEntity(CreateUserRequest request) {
        if (request == null) return null;

        User user = new User();
        user.setUserId(UUID.randomUUID().toString());
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setEmail(request.email());
        user.setPhoneNumber(request.phoneNumber());
        user.setPasswordHash(request.password());
        user.setGender(request.gender());
        user.setDateOfBirth(request.dateOfBirth());
        return user;
    }

    public UserResponse toResponse(User user) {
        if (user == null) return null;

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
                user.getStatus(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
