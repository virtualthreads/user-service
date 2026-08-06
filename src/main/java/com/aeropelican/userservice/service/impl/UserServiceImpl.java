package com.aeropelican.userservice.service.impl;

import com.aeropelican.userservice.dto.request.CreateUserRequestDTO;
import com.aeropelican.userservice.dto.request.UpdateUserRequestDTO;
import com.aeropelican.userservice.dto.response.UserResponseDTO;
import com.aeropelican.userservice.entity.User;
import com.aeropelican.userservice.enums.UserStatus;
import com.aeropelican.userservice.exception.EmailAlreadyExistsException;
import com.aeropelican.userservice.exception.PhoneNumberAlreadyExistsException;
import com.aeropelican.userservice.exception.UserNotFoundException;
import com.aeropelican.userservice.mapper.UserMapper;
import com.aeropelican.userservice.repository.UserRepository;
import com.aeropelican.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    // =========================================================
    // CREATE USER
    // =========================================================

    @Override
    public UserResponseDTO createUser(CreateUserRequestDTO request) {

        log.info("Creating user with email: {}", request.getEmail());

        if (userRepository.existsByEmail(request.getEmail())) {

            log.warn("Email already exists: {}", request.getEmail());

            throw new EmailAlreadyExistsException(
                    "Email is already registered"
            );
        }

        if (request.getPhoneNumber() != null
                && !request.getPhoneNumber().isBlank()
                && userRepository.existsByPhoneNumber(request.getPhoneNumber())) {

            log.warn("Phone number already exists: {}", request.getPhoneNumber());

            throw new PhoneNumberAlreadyExistsException(
                    "Phone number is already registered"
            );
        }

        User user = UserMapper.toEntity(request);

        user.setPasswordHash(
                passwordEncoder.encode(request.getPassword())
        );

        User savedUser = userRepository.save(user);

        log.info("User created successfully. User ID: {}", savedUser.getUserId());

        return UserMapper.toResponse(savedUser);
    }

    // =========================================================
    // GET USER
    // =========================================================

    @Override
    public UserResponseDTO getUser(UUID userId) {

        log.info("Fetching user with ID: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {

                    log.warn("User not found with ID: {}", userId);

                    return new UserNotFoundException(
                            "User not found with ID: " + userId
                    );
                });

        log.info("User fetched successfully.");

        return UserMapper.toResponse(user);
    }

    // =========================================================
    // SEARCH USERS
    // =========================================================

    @Override
    public List<UserResponseDTO> searchUsers(String keyword, String status) {

        log.info("Searching users. keyword={}, status={}", keyword, status);

        List<User> users;

        if ((keyword == null || keyword.isBlank())
                && (status == null || status.isBlank())) {

            users = userRepository.findAll();

        } else if (keyword != null && !keyword.isBlank()
                && (status == null || status.isBlank())) {

            users = userRepository
                    .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                            keyword,
                            keyword,
                            keyword
                    );

        } else if ((keyword == null || keyword.isBlank())
                && status != null && !status.isBlank()) {

            users = userRepository.findByStatus(
                    UserStatus.valueOf(status.toUpperCase())
            );

        } else {

            users = userRepository
                    .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                            keyword,
                            keyword,
                            keyword
                    )
                    .stream()
                    .filter(user -> user.getStatus().name().equalsIgnoreCase(status))
                    .toList();
        }

        log.info("Total users found: {}", users.size());

        return users.stream()
                .map(UserMapper::toResponse)
                .toList();
    }

    // =========================================================
    // UPDATE USER
    // =========================================================

    // =========================================================
// UPDATE USER
// =========================================================

    @Override
    public UserResponseDTO updateUser(
            UUID userId,
            UpdateUserRequestDTO request) {

        log.info("Updating user with ID: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {

                    log.warn("User not found with ID: {}", userId);

                    return new UserNotFoundException(
                            "User not found with ID: " + userId
                    );
                });

        if (request.getEmail() != null
                && !request.getEmail().equalsIgnoreCase(user.getEmail())
                && userRepository.existsByEmail(request.getEmail())) {

            log.warn("Email already exists: {}", request.getEmail());

            throw new EmailAlreadyExistsException(
                    "Email is already registered"
            );
        }

        if (request.getPhoneNumber() != null
                && !request.getPhoneNumber().equals(user.getPhoneNumber())
                && userRepository.existsByPhoneNumber(request.getPhoneNumber())) {

            log.warn("Phone number already exists: {}",
                    request.getPhoneNumber());

            throw new PhoneNumberAlreadyExistsException(
                    "Phone number is already registered"
            );
        }

        UserMapper.updateEntity(user, request);

        User updatedUser = userRepository.save(user);

        log.info("User updated successfully. User ID: {}",
                updatedUser.getUserId());

        return UserMapper.toResponse(updatedUser);
    }


    // =========================================================
    // UPDATE USER STATUS
    // =========================================================

    // =========================================================
// UPDATE USER STATUS
// =========================================================

    @Override
    public UserResponseDTO updateUserStatus(
            UUID userId,
            String status) {

        log.info("Updating status for user: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {

                    log.warn("User not found with ID: {}", userId);

                    return new UserNotFoundException(
                            "User not found with ID: " + userId
                    );
                });

        user.setStatus(UserStatus.valueOf(status.toUpperCase()));

        User updatedUser = userRepository.save(user);

        log.info("User status updated successfully to {}",
                updatedUser.getStatus());

        return UserMapper.toResponse(updatedUser);
    }

    // =========================================================
    // DELETE USER
    // =========================================================

    // =========================================================
// DELETE USER (SOFT DELETE)
// =========================================================

    @Override
    public void deleteUser(UUID userId) {

        log.info("Deleting user with ID: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {

                    log.warn("User not found with ID: {}", userId);

                    return new UserNotFoundException(
                            "User not found with ID: " + userId
                    );
                });

        if (user.getStatus() == UserStatus.DELETED) {

            log.warn("User already deleted. User ID: {}", userId);

            throw new IllegalArgumentException(
                    "User is already deleted."
            );
        }

        user.setStatus(UserStatus.DELETED);

        userRepository.save(user);

        log.info("User soft deleted successfully. User ID: {}", userId);
    }
}