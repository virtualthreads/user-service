package com.aeropelican.userservice.service;

import com.aeropelican.userservice.dto.request.CreateUserRequest;
import com.aeropelican.userservice.dto.request.UpdateUserRequest;
import com.aeropelican.userservice.dto.request.UpdateUserStatusRequest;
import com.aeropelican.userservice.dto.request.UserSearchRequest;
import com.aeropelican.userservice.dto.response.PageResponse;
import com.aeropelican.userservice.dto.response.UserResponse;

import java.util.UUID;

public interface UserService {
    UserResponse createUser(CreateUserRequest request);

    UserResponse getUserById(UUID userId);

    PageResponse<UserResponse> searchUsers(UserSearchRequest request);

    UserResponse updateUser(UUID userId, UpdateUserRequest request);

    UserResponse updateUserStatus(UUID userId, UpdateUserStatusRequest request);

    void deleteUser(UUID userId);

    Object getUserByEmailForAuth(String email);
}
