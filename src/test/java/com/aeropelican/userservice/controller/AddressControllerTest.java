package com.aeropelican.userservice.controller;

import com.aeropelican.userservice.dto.AddressResponse;
import com.aeropelican.userservice.dto.CreateAddressRequest;
import com.aeropelican.userservice.dto.UpdateAddressRequest;
import com.aeropelican.userservice.entity.AddressType;
import com.aeropelican.userservice.exception.GlobalExceptionHandler;
import com.aeropelican.userservice.service.AddressService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AddressControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AddressService addressService;

    @InjectMocks
    private AddressController addressController;

    private UUID userId;
    private UUID addressId;
    private AddressResponse addressResponse;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(addressController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        userId = UUID.randomUUID();
        addressId = UUID.randomUUID();

        addressResponse = new AddressResponse(
                addressId, userId, AddressType.HOME, "John Doe", "9876543210",
                "12 MG Road", "Apt 302", "Near Metro", "Bengaluru", "Karnataka", "India", "560001",
                12.971599, 77.594566, true, LocalDateTime.now(), LocalDateTime.now()
        );
    }

    @Test
    void createAddress_Returns201Created() throws Exception {
        String jsonPayload = """
                {
                    "addressType": "HOME",
                    "recipientName": "John Doe",
                    "phoneNumber": "9876543210",
                    "addressLine1": "12 MG Road",
                    "addressLine2": "Apt 302",
                    "landmark": "Near Metro",
                    "city": "Bengaluru",
                    "state": "Karnataka",
                    "country": "India",
                    "postalCode": "560001",
                    "latitude": 12.971599,
                    "longitude": 77.594566,
                    "isDefault": true
                }
                """;

        when(addressService.createAddress(eq(userId), any(CreateAddressRequest.class))).thenReturn(addressResponse);

        mockMvc.perform(post("/api/v1/users/{userId}/addresses", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.city").value("Bengaluru"));
    }

    @Test
    void getUserAddresses_Returns200OK() throws Exception {
        when(addressService.getUserAddresses(userId)).thenReturn(List.of(addressResponse));

        mockMvc.perform(get("/api/v1/users/{userId}/addresses", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].city").value("Bengaluru"));
    }

    @Test
    void getAddressById_Returns200OK() throws Exception {
        when(addressService.getAddressById(addressId)).thenReturn(addressResponse);

        mockMvc.perform(get("/api/v1/addresses/{addressId}", addressId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.addressId").value(addressId.toString()));
    }

    @Test
    void updateAddress_Returns200OK() throws Exception {
        String jsonPayload = """
                {
                    "addressType": "HOME",
                    "recipientName": "John Doe",
                    "phoneNumber": "9876543210",
                    "addressLine1": "12 MG Road",
                    "addressLine2": "Apt 302",
                    "landmark": "Near Metro",
                    "city": "Bengaluru",
                    "state": "Karnataka",
                    "country": "India",
                    "postalCode": "560001",
                    "latitude": 12.971599,
                    "longitude": 77.594566,
                    "isDefault": true
                }
                """;

        when(addressService.updateAddress(eq(addressId), any(UpdateAddressRequest.class))).thenReturn(addressResponse);

        mockMvc.perform(put("/api/v1/addresses/{addressId}", addressId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void setDefaultAddress_Returns200OK() throws Exception {
        when(addressService.setDefaultAddress(addressId)).thenReturn(addressResponse);

        mockMvc.perform(patch("/api/v1/addresses/{addressId}/default", addressId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void deleteAddress_Returns200OK() throws Exception {
        doNothing().when(addressService).deleteAddress(addressId);

        mockMvc.perform(delete("/api/v1/addresses/{addressId}", addressId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }
}
