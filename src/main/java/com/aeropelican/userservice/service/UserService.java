package com.aeropelican.userservice.service;

import com.aeropelican.userservice.dto.request.UserCreateRequestDTO;
import com.aeropelican.userservice.dto.request.UserUpdateRequestDTO;
import com.aeropelican.userservice.dto.response.UserResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {

    UserResponse createUser(UserCreateRequestDTO request);

    UserResponse getUserById(String id);

    Page<UserResponse> getAllUsers(int page, int size);

    UserResponse updateUser(String id, UserUpdateRequestDTO request);

    void deleteUser(String id);
}