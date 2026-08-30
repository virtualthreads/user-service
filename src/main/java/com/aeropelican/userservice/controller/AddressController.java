package com.aeropelican.userservice.controller;

import com.aeropelican.userservice.dto.request.CreateAddressRequest;
import com.aeropelican.userservice.dto.request.UpdateAddressRequest;
import com.aeropelican.userservice.dto.response.AddressResponse;
import com.aeropelican.userservice.dto.response.ApiResponse;
import com.aeropelican.userservice.service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
public class AddressController {

    private final AddressService addressService;

    @PostMapping("/users/{userId}/addresses")
    public ResponseEntity<ApiResponse<AddressResponse>> createAddress(@PathVariable UUID userId,
                                                                      @Valid @RequestBody CreateAddressRequest request) {
        log.info("POST /api/v1/users/{}/addresses request received", userId);
        AddressResponse response = addressService.createAddress(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<AddressResponse>builder().success(true).message("Address created successfully").data(response).timestamp(LocalDateTime.now()).build());
    }

    @GetMapping("/addresses/{addressId}")
    public ResponseEntity<ApiResponse<AddressResponse>> getAddressById(@PathVariable UUID addressId) {
        log.info("GET /api/v1/addresses/{} request received", addressId);
        return ResponseEntity.ok(ApiResponse.<AddressResponse>builder().success(true).message("Address fetched successfully").data(addressService.getAddressById(addressId)).timestamp(LocalDateTime.now()).build());
    }

    @GetMapping("/users/{userId}/addresses")
    public ResponseEntity<ApiResponse<List<AddressResponse>>> getUserAddresses(@PathVariable UUID userId) {
        log.info("GET /api/v1/users/{}/addresses request received", userId);
        return ResponseEntity.ok(ApiResponse.<List<AddressResponse>>builder().success(true).message("User addresses fetched successfully").data(addressService.getUserAddresses(userId)).timestamp(LocalDateTime.now()).build());
    }

    @PutMapping("/addresses/{addressId}")
    public ResponseEntity<ApiResponse<AddressResponse>> updateAddress(@PathVariable UUID addressId,
                                                                      @RequestParam UUID userId,
                                                                      @Valid @RequestBody UpdateAddressRequest request) {
        log.info("PUT /api/v1/addresses/{} request received", addressId);
        return ResponseEntity.ok(ApiResponse.<AddressResponse>builder().success(true).message("Address updated successfully").data(addressService.updateAddress(userId, addressId, request)).timestamp(LocalDateTime.now()).build());
    }

    @PatchMapping("/addresses/{addressId}/default")
    public ResponseEntity<ApiResponse<AddressResponse>> setDefaultAddress(@PathVariable UUID addressId,
                                                                          @RequestParam UUID userId) {
        log.info("PATCH /api/v1/addresses/{}/default request received", addressId);
        return ResponseEntity.ok(ApiResponse.<AddressResponse>builder().success(true).message("Default address updated successfully").data(addressService.setDefaultAddress(userId, addressId)).timestamp(LocalDateTime.now()).build());
    }

    @DeleteMapping("/addresses/{addressId}")
    public ResponseEntity<ApiResponse<Void>> deleteAddress(@RequestParam UUID userId,
                                                           @PathVariable UUID addressId) {
        log.info("DELETE /api/v1/addresses/{} request received", addressId);
        addressService.deleteAddress(userId, addressId);
        return ResponseEntity.ok(ApiResponse.<Void>builder().success(true).message("Address deleted successfully").timestamp(LocalDateTime.now()).build());
    }
}
