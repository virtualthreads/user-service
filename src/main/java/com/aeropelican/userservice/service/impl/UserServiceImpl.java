package com.aeropelican.userservice.service.impl;

import com.aeropelican.userservice.dto.request.CreateUserRequestDTO;
import com.aeropelican.userservice.dto.response.UserResponseDTO;
import com.aeropelican.userservice.entity.User;
import com.aeropelican.userservice.mapper.UserMapper;
import com.aeropelican.userservice.repository.UserRepository;
import com.aeropelican.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDTO createUser(CreateUserRequestDTO request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email is already registered");
        }

        if (request.getPhoneNumber() != null
                && !request.getPhoneNumber().isBlank()
                && userRepository.existsByPhoneNumber(request.getPhoneNumber())) {

            throw new IllegalArgumentException(
                    "Phone number is already registered"
            );
        }

        User user = UserMapper.toEntity(request);

        user.setPasswordHash(
                passwordEncoder.encode(request.getPassword())
        );

        User savedUser = userRepository.save(user);

        return UserMapper.toResponse(savedUser);
    }
}