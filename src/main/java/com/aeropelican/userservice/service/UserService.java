package com.aeropelican.userservice.service;

import com.aeropelican.userservice.dto.CreateUserRequest;
import com.aeropelican.userservice.dto.PageResponse;
import com.aeropelican.userservice.dto.UpdateUserRequest;
import com.aeropelican.userservice.dto.UpdateUserStatusRequest;
import com.aeropelican.userservice.dto.UserResponse;
import com.aeropelican.userservice.dto.UserSearchRequest;

import java.util.UUID;

public interface UserService {

    UserResponse createUser(CreateUserRequest request);

    UserResponse getUserById(UUID userId);

    PageResponse<UserResponse> searchUsers(UserSearchRequest request);

    UserResponse updateUser(UUID userId, UpdateUserRequest request);

    UserResponse updateUserStatus(UUID userId, UpdateUserStatusRequest request);

    void deleteUser(UUID userId);
}
