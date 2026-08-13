package com.aeropelican.userservice.service;

import com.aeropelican.userservice.dto.CreateUserRequest;
import com.aeropelican.userservice.dto.PageResponse;
import com.aeropelican.userservice.dto.SortDirection;
import com.aeropelican.userservice.dto.UpdateUserRequest;
import com.aeropelican.userservice.dto.UpdateUserStatusRequest;
import com.aeropelican.userservice.dto.UserResponse;
import com.aeropelican.userservice.dto.UserSearchRequest;
import com.aeropelican.userservice.entity.Gender;
import com.aeropelican.userservice.entity.Status;
import com.aeropelican.userservice.entity.User;
import com.aeropelican.userservice.exception.BusinessException;
import com.aeropelican.userservice.exception.ResourceAlreadyExistsException;
import com.aeropelican.userservice.exception.ResourceNotFoundException;
import com.aeropelican.userservice.exception.ValidationException;
import com.aeropelican.userservice.mapper.UserMapper;
import com.aeropelican.userservice.repository.UserRepository;
import com.aeropelican.userservice.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Spy
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private UUID userId;
    private User userEntity;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        userEntity = new User(
                userId,
                "John",
                "Doe",
                "john@example.com",
                "9876543210",
                "encodedPassword123",
                Gender.MALE,
                LocalDate.of(1995, 5, 20),
                true,
                true,
                Status.ACTIVE,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    @Test
    void createUser_Success() {
        CreateUserRequest request = new CreateUserRequest(
                "John", "Doe", "john@example.com", "9876543210", "Password123!", Gender.MALE, LocalDate.of(1995, 5, 20)
        );

        when(userRepository.existsByEmail("john@example.com")).thenReturn(false);
        when(passwordEncoder.encode("Password123!")).thenReturn("encodedPassword123");
        when(userRepository.save(any(User.class))).thenReturn(userEntity);

        UserResponse response = userService.createUser(request);

        assertNotNull(response);
        assertEquals("John", response.firstName());
        assertEquals("john@example.com", response.email());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void createUser_ThrowsResourceAlreadyExistsException_WhenEmailExists() {
        CreateUserRequest request = new CreateUserRequest(
                "John", "Doe", "john@example.com", "9876543210", "Password123!", Gender.MALE, LocalDate.of(1995, 5, 20)
        );

        when(userRepository.existsByEmail("john@example.com")).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> userService.createUser(request));
    }

    @Test
    void createUser_ThrowsValidationException_WhenPasswordShort() {
        CreateUserRequest request = new CreateUserRequest(
                "John", "Doe", "john@example.com", "9876543210", "pass", Gender.MALE, LocalDate.of(1995, 5, 20)
        );

        assertThrows(ValidationException.class, () -> userService.createUser(request));
    }

    @Test
    void getUserById_Success() {
        when(userRepository.findByUserIdAndStatusNot(userId, Status.DELETED)).thenReturn(Optional.of(userEntity));

        UserResponse response = userService.getUserById(userId);

        assertNotNull(response);
        assertEquals(userId, response.userId());
        assertEquals("john@example.com", response.email());
    }

    @Test
    void getUserById_ThrowsResourceNotFoundException_WhenNotFound() {
        when(userRepository.findByUserIdAndStatusNot(userId, Status.DELETED)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(userId));
    }

    @Test
    void updateUser_Success() {
        UpdateUserRequest request = new UpdateUserRequest(
                "Johnathan", "Doe", "john.updated@example.com", "9876543210", Gender.MALE, LocalDate.of(1995, 5, 20)
        );

        when(userRepository.findByUserIdAndStatusNot(userId, Status.DELETED)).thenReturn(Optional.of(userEntity));
        when(userRepository.existsByEmailAndUserIdNot("john.updated@example.com", userId)).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserResponse response = userService.updateUser(userId, request);

        assertNotNull(response);
        assertEquals("Johnathan", response.firstName());
        assertEquals("john.updated@example.com", response.email());
    }

    @Test
    void updateUserStatus_Success() {
        UpdateUserStatusRequest request = new UpdateUserStatusRequest(Status.LOCKED);

        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserResponse response = userService.updateUserStatus(userId, request);

        assertNotNull(response);
        assertEquals(Status.LOCKED, response.status());
    }

    @Test
    void deleteUser_Success() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));

        userService.deleteUser(userId);

        assertEquals(Status.DELETED, userEntity.getStatus());
        verify(userRepository).save(userEntity);
    }

    @Test
    void deleteUser_ThrowsBusinessException_WhenAlreadyDeleted() {
        userEntity.setStatus(Status.DELETED);
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));

        assertThrows(BusinessException.class, () -> userService.deleteUser(userId));
    }

    @Test
    @SuppressWarnings("unchecked")
    void searchUsers_Success() {
        UserSearchRequest searchRequest = new UserSearchRequest(
                "john", Status.ACTIVE, Gender.MALE, true, true, 0, 10, "firstName", SortDirection.ASC
        );

        Page<User> page = new PageImpl<>(List.of(userEntity));
        when(userRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        PageResponse<UserResponse> result = userService.searchUsers(searchRequest);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("John", result.getContent().get(0).firstName());
    }
}
