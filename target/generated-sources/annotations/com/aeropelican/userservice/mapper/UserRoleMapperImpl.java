package com.aeropelican.userservice.mapper;

import com.aeropelican.userservice.dto.response.UserRoleResponseDTO;
import com.aeropelican.userservice.entity.Role;
import com.aeropelican.userservice.entity.User;
import com.aeropelican.userservice.entity.UserRole;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-06T17:41:00+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class UserRoleMapperImpl implements UserRoleMapper {

    @Override
    public UserRoleResponseDTO toResponseDTO(UserRole userRole) {
        if ( userRole == null ) {
            return null;
        }

        UserRoleResponseDTO userRoleResponseDTO = new UserRoleResponseDTO();

        String userId = userRoleUserUserId( userRole );
        if ( userId != null ) {
            userRoleResponseDTO.setUserId( UUID.fromString( userId ) );
        }
        userRoleResponseDTO.setRoleId( userRoleRoleRoleId( userRole ) );
        userRoleResponseDTO.setUserRoleId( userRole.getUserRoleId() );
        userRoleResponseDTO.setAssignedAt( userRole.getAssignedAt() );

        return userRoleResponseDTO;
    }

    private String userRoleUserUserId(UserRole userRole) {
        if ( userRole == null ) {
            return null;
        }
        User user = userRole.getUser();
        if ( user == null ) {
            return null;
        }
        String userId = user.getUserId();
        if ( userId == null ) {
            return null;
        }
        return userId;
    }

    private UUID userRoleRoleRoleId(UserRole userRole) {
        if ( userRole == null ) {
            return null;
        }
        Role role = userRole.getRole();
        if ( role == null ) {
            return null;
        }
        UUID roleId = role.getRoleId();
        if ( roleId == null ) {
            return null;
        }
        return roleId;
    }
}
