package com.aeropelican.userservice.mapper;

import com.aeropelican.userservice.dto.request.UserCreateRequestDTO;
import com.aeropelican.userservice.dto.request.UserUpdateRequestDTO;
import com.aeropelican.userservice.dto.response.UserResponseDTO;
import com.aeropelican.userservice.entity.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-05T16:24:50+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User toEntity(UserCreateRequestDTO dto) {
        if ( dto == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.firstName( dto.getFirstName() );
        user.lastName( dto.getLastName() );
        user.email( dto.getEmail() );
        user.phoneNumber( dto.getPhoneNumber() );
        user.gender( dto.getGender() );
        user.dateOfBirth( dto.getDateOfBirth() );

        return user.build();
    }

    @Override
    public UserResponseDTO toResponse(User user) {
        if ( user == null ) {
            return null;
        }

        UserResponseDTO.UserResponseDTOBuilder userResponseDTO = UserResponseDTO.builder();

        userResponseDTO.userId( user.getUserId() );
        userResponseDTO.firstName( user.getFirstName() );
        userResponseDTO.lastName( user.getLastName() );
        userResponseDTO.email( user.getEmail() );
        userResponseDTO.phoneNumber( user.getPhoneNumber() );
        userResponseDTO.gender( user.getGender() );
        userResponseDTO.dateOfBirth( user.getDateOfBirth() );
        userResponseDTO.emailVerified( user.getEmailVerified() );
        userResponseDTO.phoneVerified( user.getPhoneVerified() );
        userResponseDTO.status( user.getStatus() );
        userResponseDTO.createdAt( user.getCreatedAt() );
        userResponseDTO.updatedAt( user.getUpdatedAt() );

        return userResponseDTO.build();
    }

    @Override
    public void updateEntity(UserUpdateRequestDTO dto, User user) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getFirstName() != null ) {
            user.setFirstName( dto.getFirstName() );
        }
        if ( dto.getLastName() != null ) {
            user.setLastName( dto.getLastName() );
        }
        if ( dto.getEmail() != null ) {
            user.setEmail( dto.getEmail() );
        }
        if ( dto.getPhoneNumber() != null ) {
            user.setPhoneNumber( dto.getPhoneNumber() );
        }
        if ( dto.getGender() != null ) {
            user.setGender( dto.getGender() );
        }
        if ( dto.getDateOfBirth() != null ) {
            user.setDateOfBirth( dto.getDateOfBirth() );
        }
        if ( dto.getEmailVerified() != null ) {
            user.setEmailVerified( dto.getEmailVerified() );
        }
        if ( dto.getPhoneVerified() != null ) {
            user.setPhoneVerified( dto.getPhoneVerified() );
        }
        if ( dto.getStatus() != null ) {
            user.setStatus( dto.getStatus() );
        }
    }
}
