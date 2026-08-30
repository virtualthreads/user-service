package com.aeropelican.userservice.service;

import com.aeropelican.userservice.dto.request.CreateAddressRequest;
import com.aeropelican.userservice.dto.request.UpdateAddressRequest;
import com.aeropelican.userservice.dto.response.AddressResponse;

import java.util.List;
import java.util.UUID;

public interface AddressService {
    AddressResponse createAddress(UUID userId, CreateAddressRequest request);

    AddressResponse getAddressById(UUID addressId);

    List<AddressResponse> getUserAddresses(UUID userId);

    AddressResponse updateAddress(UUID userId, UUID addressId, UpdateAddressRequest request);

    AddressResponse setDefaultAddress(UUID userId, UUID addressId);

    void deleteAddress(UUID userId, UUID addressId);
}
