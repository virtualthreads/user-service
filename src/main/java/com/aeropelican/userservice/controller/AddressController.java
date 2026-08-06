package com.aeropelican.userservice.controller;

import com.aeropelican.userservice.dto.Response.AddressResponse;
import com.aeropelican.userservice.dto.Response.ApiResponse;
import com.aeropelican.userservice.dto.Request.CreateAddressRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users/{userId}/addresses")
public class AddressController {

    private final com.aeropelican.userservice.service.Addressservice addressService;

    public AddressController(com.aeropelican.userservice.service.Addressservice addressService) {
        this.addressService = addressService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AddressResponse>> createAddress(
            @PathVariable String userId,
            @RequestBody CreateAddressRequest request) {
        AddressResponse response = addressService.createAddress(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Address created successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AddressResponse>>> getUserAddresses(
            @PathVariable String userId) {
        List<AddressResponse> responses = addressService.getUserAddresses(userId);
        return ResponseEntity.ok(ApiResponse.success("Addresses retrieved", responses));
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<ApiResponse<Void>> deleteAddress(
            @PathVariable String addressId) {
        addressService.deleteAddress(addressId);
        return ResponseEntity.ok(ApiResponse.success("Address deleted successfully", null));
    }
}