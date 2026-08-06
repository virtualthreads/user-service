package com.aeropelican.userservice.service;

import com.aeropelican.userservice.dto.Response.AddressResponse;
import com.aeropelican.userservice.dto.Request.CreateAddressRequest;
import com.aeropelican.userservice.entity.Address;
import com.aeropelican.userservice.exception.ResourceInUseException;
import com.aeropelican.userservice.exception.ResourceNotFoundException;
import com.aeropelican.userservice.repository.AddressRepository;
import com.aeropelican.userservice.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class Addressservice {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    public Addressservice(AddressRepository addressRepository, UserRepository userRepository) {
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public AddressResponse createAddress(String userId, CreateAddressRequest req) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found");
        }

        List<Address> userAddresses = addressRepository.findByUserId(userId);
        boolean setAsDefault = Boolean.TRUE.equals(req.isDefault()) || userAddresses.isEmpty();

        if (setAsDefault) {
            userAddresses.forEach(a -> {
                a.setIsDefault(false);
                addressRepository.save(a);
            });
        }

        Address address = new Address();
        address.setAddressId(UUID.randomUUID().toString());
        address.setUserId(userId);
        address.setAddressType(req.addressType());
        address.setRecipientName(req.recipientName());
        address.setPhoneNumber(req.phoneNumber());
        address.setAddressLine1(req.addressLine1());
        address.setAddressLine2(req.addressLine2());
        address.setLandmark(req.landmark());
        address.setCity(req.city());
        address.setState(req.state());
        address.setCountry(req.country());
        address.setPostalCode(req.postalCode());
        address.setLatitude(req.latitude());
        address.setLongitude(req.longitude());
        address.setIsDefault(setAsDefault);

        Address saved = addressRepository.save(address);
        return mapToResponse(saved);
    }

    public List<AddressResponse> getUserAddresses(String userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found");
        }
        return addressRepository.findByUserId(userId).stream().map(this::mapToResponse).toList();
    }

    @Transactional
    public void deleteAddress(String addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found"));
        if (Boolean.TRUE.equals(address.getIsDefault())) {
            throw new ResourceInUseException("Cannot delete default address directly. Mark another as default first.");
        }
        addressRepository.delete(address);
    }

    private AddressResponse mapToResponse(Address a) {
        return new AddressResponse(
                a.getAddressId(), a.getUserId(), a.getAddressType(), a.getRecipientName(),
                a.getPhoneNumber(), a.getAddressLine1(), a.getAddressLine2(), a.getLandmark(),
                a.getCity(), a.getState(), a.getCountry(), a.getPostalCode(),
                a.getLatitude(), a.getLongitude(), a.getIsDefault()
        );
    }
}