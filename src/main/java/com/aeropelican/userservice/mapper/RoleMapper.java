package com.aeropelican.userservice.mapper;

import com.aeropelican.userservice.dto.request.CreateRoleRequest;
import com.aeropelican.userservice.dto.response.RoleResponse;
import com.aeropelican.userservice.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoleMapper {

    @Mapping(target = "roleId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Role toEntity(CreateRoleRequest request);

    RoleResponse toResponse(Role role);
}
