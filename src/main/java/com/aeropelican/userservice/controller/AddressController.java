package com.aeropelican.userservice.controller;

import com.aeropelican.userservice.dto.AddressResponse;
import com.aeropelican.userservice.dto.ApiResponse;
import com.aeropelican.userservice.dto.CreateAddressRequest;
import com.aeropelican.userservice.dto.UpdateAddressRequest;
import com.aeropelican.userservice.service.AddressService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class AddressController {

    private static final Logger logger = LoggerFactory.getLogger(AddressController.class);

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @PostMapping("/users/{userId}/addresses")
    public ResponseEntity<ApiResponse<AddressResponse>> createAddress(
            @PathVariable UUID userId,
            @Valid @RequestBody CreateAddressRequest request) {
        logger.info("REST request to create address for user ID: {}", userId);
        AddressResponse createdAddress = addressService.createAddress(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(createdAddress, "Address created successfully"));
    }

    @GetMapping("/users/{userId}/addresses")
    public ResponseEntity<ApiResponse<List<AddressResponse>>> getUserAddresses(@PathVariable UUID userId) {
        logger.info("REST request to fetch addresses for user ID: {}", userId);
        List<AddressResponse> addresses = addressService.getUserAddresses(userId);
        return ResponseEntity.ok(ApiResponse.success(addresses, "User addresses retrieved successfully"));
    }

    @GetMapping("/addresses/{addressId}")
    public ResponseEntity<ApiResponse<AddressResponse>> getAddressById(@PathVariable UUID addressId) {
        logger.info("REST request to get address by ID: {}", addressId);
        AddressResponse address = addressService.getAddressById(addressId);
        return ResponseEntity.ok(ApiResponse.success(address, "Address retrieved successfully"));
    }

    @PutMapping("/addresses/{addressId}")
    public ResponseEntity<ApiResponse<AddressResponse>> updateAddress(
            @PathVariable UUID addressId,
            @Valid @RequestBody UpdateAddressRequest request) {
        logger.info("REST request to update address ID: {}", addressId);
        AddressResponse updatedAddress = addressService.updateAddress(addressId, request);
        return ResponseEntity.ok(ApiResponse.success(updatedAddress, "Address updated successfully"));
    }

    @PatchMapping("/addresses/{addressId}/default")
    public ResponseEntity<ApiResponse<AddressResponse>> setDefaultAddress(@PathVariable UUID addressId) {
        logger.info("REST request to set address ID {} as default", addressId);
        AddressResponse updatedAddress = addressService.setDefaultAddress(addressId);
        return ResponseEntity.ok(ApiResponse.success(updatedAddress, "Default address set successfully"));
    }

    @DeleteMapping("/addresses/{addressId}")
    public ResponseEntity<ApiResponse<Void>> deleteAddress(@PathVariable UUID addressId) {
        logger.info("REST request to delete address ID: {}", addressId);
        addressService.deleteAddress(addressId);
        return ResponseEntity.ok(ApiResponse.success(null, "Address deleted successfully"));
    }
}
