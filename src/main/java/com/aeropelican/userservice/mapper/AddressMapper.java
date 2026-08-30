package com.aeropelican.userservice.mapper;

import com.aeropelican.userservice.dto.request.CreateAddressRequest;
import com.aeropelican.userservice.dto.request.UpdateAddressRequest;
import com.aeropelican.userservice.dto.response.AddressResponse;
import com.aeropelican.userservice.entity.Address;
import com.aeropelican.userservice.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AddressMapper {

    @Mapping(target = "addressId", ignore = true)
    @Mapping(target = "user", source = "user")
    @Mapping(target = "addressType", source = "request.addressType")
    @Mapping(target = "recipientName", source = "request.recipientName")
    @Mapping(target = "phoneNumber", source = "request.phoneNumber")
    @Mapping(target = "addressLine1", source = "request.addressLine1")
    @Mapping(target = "addressLine2", source = "request.addressLine2")
    @Mapping(target = "landmark", source = "request.landmark")
    @Mapping(target = "city", source = "request.city")
    @Mapping(target = "state", source = "request.state")
    @Mapping(target = "country", source = "request.country")
    @Mapping(target = "postalCode", source = "request.postalCode")
    @Mapping(target = "latitude", source = "request.latitude")
    @Mapping(target = "longitude", source = "request.longitude")
    @Mapping(target = "isDefault", source = "request.isDefault")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Address toEntity(CreateAddressRequest request, User user);

    @Mapping(target = "addressType", source = "request.addressType")
    @Mapping(target = "recipientName", source = "request.recipientName")
    @Mapping(target = "phoneNumber", source = "request.phoneNumber")
    @Mapping(target = "addressLine1", source = "request.addressLine1")
    @Mapping(target = "addressLine2", source = "request.addressLine2")
    @Mapping(target = "landmark", source = "request.landmark")
    @Mapping(target = "city", source = "request.city")
    @Mapping(target = "state", source = "request.state")
    @Mapping(target = "country", source = "request.country")
    @Mapping(target = "postalCode", source = "request.postalCode")
    @Mapping(target = "latitude", source = "request.latitude")
    @Mapping(target = "longitude", source = "request.longitude")
    @Mapping(target = "isDefault", source = "request.isDefault")
    @Mapping(target = "addressId", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDto(UpdateAddressRequest request, @MappingTarget Address address);

    @Mapping(target = "userId", source = "user.userId")
    @Mapping(target = "addressId", source = "addressId")
    @Mapping(target = "addressType", source = "addressType")
    @Mapping(target = "recipientName", source = "recipientName")
    @Mapping(target = "phoneNumber", source = "phoneNumber")
    @Mapping(target = "addressLine1", source = "addressLine1")
    @Mapping(target = "addressLine2", source = "addressLine2")
    @Mapping(target = "landmark", source = "landmark")
    @Mapping(target = "city", source = "city")
    @Mapping(target = "state", source = "state")
    @Mapping(target = "country", source = "country")
    @Mapping(target = "postalCode", source = "postalCode")
    @Mapping(target = "latitude", source = "latitude")
    @Mapping(target = "longitude", source = "longitude")
    @Mapping(target = "isDefault", source = "isDefault")
    AddressResponse toResponse(Address address);
}
