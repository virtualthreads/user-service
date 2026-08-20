package com.aeropelican.userservice.mapper;

import com.aeropelican.userservice.dto.request.CreateRoleRequest;
import com.aeropelican.userservice.dto.response.RoleResponse;
import com.aeropelican.userservice.dto.request.UpdateRoleRequest;
import com.aeropelican.userservice.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    Role toEntity(CreateRoleRequest request);

    void updateEntityFromDto(UpdateRoleRequest request, @MappingTarget Role role);

    RoleResponse toResponse(Role role);
}
