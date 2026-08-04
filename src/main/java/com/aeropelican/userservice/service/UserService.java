package com.aeropelican.userservice.service;

import com.aeropelican.userservice.dto.request.CreateUserRequestDTO;
import com.aeropelican.userservice.dto.response.UserResponseDTO;

public interface UserService {

    UserResponseDTO createUser(CreateUserRequestDTO request);
}