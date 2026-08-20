package com.aeropelican.userservice.mapper;

import com.aeropelican.userservice.dto.request.CreateUserRequest;
import com.aeropelican.userservice.dto.request.UpdateUserRequest;
import com.aeropelican.userservice.dto.response.UserResponse;
import com.aeropelican.userservice.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "passwordHash", source = "encodedPassword")
    @Mapping(target = "emailVerified", constant = "false")
    @Mapping(target = "phoneVerified", constant = "false")
    @Mapping(target = "status", expression = "java(com.aeropelican.userservice.entity.Status.ACTIVE)")
    User toEntity(CreateUserRequest request, String encodedPassword);

    void updateEntityFromDto(UpdateUserRequest request, @MappingTarget User user);

    UserResponse toResponse(User user);
}
