package com.aeropelican.userservice.service;

import com.aeropelican.userservice.dto.request.AddressCreateRequestDTO;
import com.aeropelican.userservice.dto.request.AddressUpdateRequestDTO;
import com.aeropelican.userservice.dto.response.AddressResponse;
import org.springframework.data.domain.Page;

public interface AddressService {

    AddressResponse createAddress(AddressCreateRequestDTO request);

    AddressResponse getAddressById(String id);

    Page<AddressResponse> getAllAddresses(int page, int size);

    AddressResponse updateAddress(
            String id,
            AddressUpdateRequestDTO request);

    void deleteAddress(String id);

}