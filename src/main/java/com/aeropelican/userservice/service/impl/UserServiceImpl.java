package com.aeropelican.userservice.service.impl;
import com.aeropelican.userservice.dto.request.UserCreateRequestDTO;
import com.aeropelican.userservice.dto.request.UserUpdateRequestDTO;
import com.aeropelican.userservice.dto.response.UserResponse;
import com.aeropelican.userservice.entity.User;
import com.aeropelican.userservice.exception.UserNotFoundException;
import com.aeropelican.userservice.mapper.UserMapper;
import com.aeropelican.userservice.repository.UserRepository;
import com.aeropelican.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserResponse createUser(UserCreateRequestDTO request) {

        log.info("Creating user with email: {}", request.email());

        if (userRepository.existsByEmail(request.email())) {

            log.error("User already exists with email: {}", request.email());

            throw new RuntimeException("Email already exists.");
        }

        User user = UserMapper.toEntity(request);

        userRepository.save(user);

        log.info("User saved successfully with id: {}", user.getUserId());

        return UserMapper.toResponse(user);
    }
    @Override
    public UserResponse getUserById(String id) {

        log.info("Fetching user with id: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> {

                    log.error("User not found with id: {}", id);

                    return new UserNotFoundException(
                            "User not found with id: " + id);
                });

        log.info("User fetched successfully");

        return UserMapper.toResponse(user);
    }
    @Override
    public Page<UserResponse> getAllUsers(int page, int size) {

        log.info("Fetching users. Page: {}, Size: {}", page, size);

        Pageable pageable = PageRequest.of(page, size);

        Page<UserResponse> users = userRepository.findAll(pageable)
                .map(UserMapper::toResponse);

        log.info("Fetched {} users", users.getNumberOfElements());

        return users;
    }@Override
    public UserResponse updateUser(String id,
                                   UserUpdateRequestDTO request) {

        log.info("Updating user with id: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> {

                    log.error("User not found with id: {}", id);

                    return new UserNotFoundException(
                            "User not found with id: " + id);
                });

        UserMapper.updateEntity(user, request);

        userRepository.save(user);

        log.info("User updated successfully");

        return UserMapper.toResponse(user);
    }@Override
    public void deleteUser(String id) {

        log.info("Deleting user with id: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> {

                    log.error("User not found with id: {}", id);

                    return new UserNotFoundException(
                            "User not found with id: " + id);
                });

        userRepository.delete(user);

        log.info("User deleted successfully");
    }
}