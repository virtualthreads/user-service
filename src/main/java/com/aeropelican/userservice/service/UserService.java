package com.aeropelican.userservice.service;

import com.aeropelican.userservice.dto.Request.CreateUserRequest;
import com.aeropelican.userservice.dto.Request.UpdateUserRequest;
import com.aeropelican.userservice.dto.Response.UserResponse;
import com.aeropelican.userservice.entity.User;
import com.aeropelican.userservice.entity.enums.UserStatus;
import com.aeropelican.userservice.exception.ResourceAlreadyExistsException;
import com.aeropelican.userservice.exception.ResourceNotFoundException;
import com.aeropelican.userservice.exception.ValidationException;
import com.aeropelican.userservice.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public UserResponse createUser(CreateUserRequest request) {
        if (request.dateOfBirth() != null && request.dateOfBirth().isAfter(LocalDate.now())) {
            throw new ValidationException("Date of birth cannot be in the future");
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new ResourceAlreadyExistsException("Email already exists: " + request.email());
        }

        User user = new User();
        user.setUserId(UUID.randomUUID().toString());
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setEmail(request.email());
        user.setPhoneNumber(request.phoneNumber());
        user.setPasswordHash(request.password());
        user.setGender(request.gender());
        user.setDateOfBirth(request.dateOfBirth());

        User saved = userRepository.save(user);
        return mapToResponse(saved);
    }

    public UserResponse getUserById(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
        if (user.getStatus() == UserStatus.DELETED) {
            throw new ResourceNotFoundException("User not found or deleted");
        }
        return mapToResponse(user);
    }

    @Transactional
    public UserResponse updateUser(String userId, UpdateUserRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        if (request.dateOfBirth() != null && request.dateOfBirth().isAfter(LocalDate.now())) {
            throw new ValidationException("Date of birth cannot be in the future");
        }

        if (!user.getEmail().equalsIgnoreCase(request.email()) && userRepository.existsByEmail(request.email())) {
            throw new ResourceAlreadyExistsException("Email already in use: " + request.email());
        }

        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setEmail(request.email());
        user.setPhoneNumber(request.phoneNumber());
        user.setGender(request.gender());
        user.setDateOfBirth(request.dateOfBirth());

        return mapToResponse(userRepository.save(user));
    }

    @Transactional
    public UserResponse updateStatus(String userId, UserStatus status) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
        user.setStatus(status);
        return mapToResponse(userRepository.save(user));
    }

    @Transactional
    public void deleteUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
        if (user.getStatus() == UserStatus.DELETED) {
            throw new ValidationException("User is already deleted");
        }
        user.setStatus(UserStatus.DELETED);
        userRepository.save(user);
    }

    private UserResponse mapToResponse(User user) {
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