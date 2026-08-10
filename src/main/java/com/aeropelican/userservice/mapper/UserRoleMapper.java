package com.aeropelican.userservice.mapper;

import com.aeropelican.userservice.dto.response.UserRoleResponseDTO;
import com.aeropelican.userservice.entity.UserRole;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserRoleMapper {

    @Mapping(target = "userId", source = "user.userId")
    @Mapping(target = "roleId", source = "role.roleId")
    UserRoleResponseDTO toResponseDTO(UserRole userRole);
}
