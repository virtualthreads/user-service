package com.aeropelican.userservice.service;

import com.aeropelican.userservice.dto.AddressResponse;
import com.aeropelican.userservice.dto.CreateAddressRequest;
import com.aeropelican.userservice.dto.UpdateAddressRequest;
import com.aeropelican.userservice.entity.Address;
import com.aeropelican.userservice.entity.AddressType;
import com.aeropelican.userservice.entity.Gender;
import com.aeropelican.userservice.entity.Status;
import com.aeropelican.userservice.entity.User;
import com.aeropelican.userservice.exception.ResourceInUseException;
import com.aeropelican.userservice.exception.ResourceNotFoundException;
import com.aeropelican.userservice.mapper.AddressMapper;
import com.aeropelican.userservice.repository.AddressRepository;
import com.aeropelican.userservice.repository.UserRepository;
import com.aeropelican.userservice.service.impl.AddressServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AddressServiceImplTest {

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private UserRepository userRepository;

    @Spy
    private AddressMapper addressMapper;

    @InjectMocks
    private AddressServiceImpl addressService;

    private UUID userId;
    private UUID addressId;
    private User user;
    private Address address;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        addressId = UUID.randomUUID();

        user = new User(userId, "John", "Doe", "john@example.com", "9876543210", "pass",
                Gender.MALE, LocalDate.of(1995, 5, 20), true, true, Status.ACTIVE, LocalDateTime.now(), LocalDateTime.now());

        address = new Address(
                addressId, user, AddressType.HOME, "John Doe", "9876543210",
                "12 MG Road", "Apt 302", "Near Metro", "Bengaluru", "Karnataka", "India", "560001",
                12.971599, 77.594566, true, LocalDateTime.now(), LocalDateTime.now()
        );
    }

    @Test
    void createAddress_Success() {
        CreateAddressRequest request = new CreateAddressRequest(
                AddressType.HOME, "John Doe", "9876543210", "12 MG Road", "Apt 302",
                "Near Metro", "Bengaluru", "Karnataka", "India", "560001", 12.971599, 77.594566, true
        );

        when(userRepository.findByUserIdAndStatusNot(userId, Status.DELETED)).thenReturn(Optional.of(user));
        when(addressRepository.save(any(Address.class))).thenReturn(address);

        AddressResponse response = addressService.createAddress(userId, request);

        assertNotNull(response);
        assertEquals("Bengaluru", response.city());
        assertTrue(response.isDefault());
        verify(addressRepository).clearDefaultAddressesForUser(userId);
        verify(addressRepository).save(any(Address.class));
    }

    @Test
    void getAddressById_Success() {
        when(addressRepository.findById(addressId)).thenReturn(Optional.of(address));

        AddressResponse response = addressService.getAddressById(addressId);

        assertNotNull(response);
        assertEquals(addressId, response.addressId());
    }

    @Test
    void getAddressById_ThrowsResourceNotFoundException_WhenNotFound() {
        when(addressRepository.findById(addressId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> addressService.getAddressById(addressId));
    }

    @Test
    void setDefaultAddress_Success() {
        address.setIsDefault(false);
        when(addressRepository.findById(addressId)).thenReturn(Optional.of(address));
        when(addressRepository.save(any(Address.class))).thenReturn(address);

        AddressResponse response = addressService.setDefaultAddress(addressId);

        assertNotNull(response);
        assertTrue(response.isDefault());
        verify(addressRepository).clearDefaultAddressesForUser(userId);
    }

    @Test
    void deleteAddress_ThrowsResourceInUseException_WhenDefaultAndOtherAddressesExist() {
        when(addressRepository.findById(addressId)).thenReturn(Optional.of(address));
        when(addressRepository.countByUser_UserId(userId)).thenReturn(2L);

        assertThrows(ResourceInUseException.class, () -> addressService.deleteAddress(addressId));
    }

    @Test
    void deleteAddress_Success_WhenOnlyAddress() {
        when(addressRepository.findById(addressId)).thenReturn(Optional.of(address));
        when(addressRepository.countByUser_UserId(userId)).thenReturn(1L);

        addressService.deleteAddress(addressId);

        verify(addressRepository).delete(address);
    }
}
