package com.aeropelican.userservice.service;

import com.aeropelican.userservice.dto.request.UserCreateRequestDTO;
import com.aeropelican.userservice.dto.request.UserUpdateRequestDTO;
import com.aeropelican.userservice.dto.response.UserResponseDTO;
import com.aeropelican.userservice.entity.User;
import com.aeropelican.userservice.exceptions.UserNotFoundException;
import com.aeropelican.userservice.mapper.UserMapper;
import com.aeropelican.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserResponseDTO createUser(UserCreateRequestDTO requestDTO) {

        if (userRepository.existsByEmail(requestDTO.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = userMapper.toEntity(requestDTO);

        // Temporary: storing plain password.
        // Later we'll replace this with BCrypt password encoding.
        user.setPasswordHash(requestDTO.getPassword());

        User savedUser = userRepository.save(user);

        return userMapper.toResponse(savedUser);
    }

    public UserResponseDTO getUserById(String userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        return userMapper.toResponse(user);
    }

    public List<UserResponseDTO> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponse)
                .toList();
    }

    public UserResponseDTO updateUser(String userId, UserUpdateRequestDTO requestDTO) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        userMapper.updateEntity(requestDTO, user);

        User updatedUser = userRepository.save(user);

        return userMapper.toResponse(updatedUser);
    }

    public void deleteUser(String userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        userRepository.delete(user);
    }
}
