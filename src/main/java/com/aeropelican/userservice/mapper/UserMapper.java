package com.aeropelican.userservice.mapper;

import com.aeropelican.userservice.dto.request.UserCreateRequestDTO;
import com.aeropelican.userservice.dto.request.UserUpdateRequestDTO;
import com.aeropelican.userservice.dto.response.UserResponseDTO;
import com.aeropelican.userservice.entity.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(UserCreateRequestDTO dto);

    UserResponseDTO toResponse(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(UserUpdateRequestDTO dto, @MappingTarget User user);
}
