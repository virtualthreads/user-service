package com.aeropelican.userservice.mapper;

import com.aeropelican.userservice.dto.request.CreateAddressRequest;
import com.aeropelican.userservice.dto.request.UpdateAddressRequest;
import com.aeropelican.userservice.dto.response.AddressResponse;
import com.aeropelican.userservice.entity.Address;
import com.aeropelican.userservice.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    @Mapping(target = "user", source = "user")
    @Mapping(target = "isDefault", source = "request.isDefault")
    @Mapping(target = "phoneNumber", source = "request.phoneNumber")
    Address toEntity(CreateAddressRequest request, User user);

    void updateEntityFromDto(UpdateAddressRequest request, @MappingTarget Address address);

    @Mapping(target = "userId", source = "user.userId")
    AddressResponse toResponse(Address address);
}
