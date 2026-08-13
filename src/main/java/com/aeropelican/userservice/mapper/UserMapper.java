package com.aeropelican.userservice.mapper;

import com.aeropelican.userservice.dto.CreateUserRequest;
import com.aeropelican.userservice.dto.UpdateUserRequest;
import com.aeropelican.userservice.dto.UserResponse;
import com.aeropelican.userservice.entity.Status;
import com.aeropelican.userservice.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserMapper {

    private static final Logger logger = LoggerFactory.getLogger(UserMapper.class);

    public User toEntity(CreateUserRequest request, String encodedPassword) {
        logger.debug("Mapping CreateUserRequest to User entity for email: {}", request.email());
        User user = new User();
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setEmail(request.email());
        user.setPhoneNumber(request.phoneNumber());
        user.setPasswordHash(encodedPassword);
        user.setGender(request.gender());
        user.setDateOfBirth(request.dateOfBirth());
        user.setEmailVerified(false);
        user.setPhoneVerified(false);
        user.setStatus(Status.ACTIVE);
        return user;
    }

    public void updateEntityFromDto(UpdateUserRequest request, User user) {
        logger.debug("Updating User entity (userId: {}) from UpdateUserRequest", user.getUserId());
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setEmail(request.email());
        user.setPhoneNumber(request.phoneNumber());
        user.setGender(request.gender());
        user.setDateOfBirth(request.dateOfBirth());
        user.setUpdatedAt(LocalDateTime.now());
    }

    public UserResponse toResponse(User user) {
        if (user == null) {
            logger.debug("Attempted to map null User entity to UserResponse");
            return null;
        }
        logger.debug("Mapping User entity to UserResponse for userId: {}", user.getUserId());
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
