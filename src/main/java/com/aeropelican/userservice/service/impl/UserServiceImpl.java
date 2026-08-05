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
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserResponse createUser(UserCreateRequestDTO request) {

        if (userRepository.existsByEmail(request.email())) {
            throw new RuntimeException("Email already exists.");
        }

        User user = UserMapper.toEntity(request);

        userRepository.save(user);

        return UserMapper.toResponse(user);
    }

    @Override
    public UserResponse getUserById(String id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found with id: " + id));

        return UserMapper.toResponse(user);
    }

    @Override
    public List<UserResponse> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(UserMapper::toResponse)
                .toList();
    }

    @Override
    public UserResponse updateUser(String id, UserUpdateRequestDTO request) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found with id: " + id));

        UserMapper.updateEntity(user, request);

        userRepository.save(user);

        return UserMapper.toResponse(user);
    }

    @Override
    public void deleteUser(String id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found with id: " + id));

        userRepository.delete(user);
    }
}