package com.aeropelican.userservice.service;

import com.aeropelican.userservice.dto.SortDirection;
import com.aeropelican.userservice.dto.request.CreateUserRequest;
import com.aeropelican.userservice.dto.request.UpdateUserRequest;
import com.aeropelican.userservice.dto.request.UpdateUserStatusRequest;
import com.aeropelican.userservice.dto.request.UserSearchRequest;
import com.aeropelican.userservice.dto.response.PageResponse;
import com.aeropelican.userservice.dto.response.UserResponse;
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private UUID userId;
    private User user;
    private UserResponse userResponse;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        user = new User(
                userId,
                "John",
                "Doe",
                "john.doe@example.com",
                "9876543210",
                "encoded_password_hash",
                Gender.MALE,
                LocalDate.of(1990, 1, 1),
                true,
                false,
                Status.ACTIVE,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        userResponse = new UserResponse(
                userId,
                "John",
                "Doe",
                "john.doe@example.com",
                "9876543210",
                Gender.MALE,
                LocalDate.of(1990, 1, 1),
                true,
                false,
                Status.ACTIVE,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    @Test
    @DisplayName("Create User - Successfully saves and returns response")
    void testCreateUser_Success() {
        CreateUserRequest request = new CreateUserRequest(
                "John",
                "Doe",
                "john.doe@example.com",
                "9876543210",
                "SecretPassword123",
                Gender.MALE,
                LocalDate.of(1990, 1, 1)
        );

        when(userRepository.existsByEmail(request.email())).thenReturn(false);
        when(passwordEncoder.encode(request.password())).thenReturn("encoded_password_hash");
        when(userMapper.toEntity(request, "encoded_password_hash")).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toResponse(user)).thenReturn(userResponse);

        UserResponse result = userService.createUser(request);

        assertThat(result).isNotNull();
        assertThat(result.userId()).isEqualTo(userId);
        assertThat(result.email()).isEqualTo("john.doe@example.com");
        verify(userRepository, times(1)).save(user);
    }

    @Test
    @DisplayName("Create User - Duplicate Email throws ResourceAlreadyExistsException")
    void testCreateUser_DuplicateEmail() {
        CreateUserRequest request = new CreateUserRequest(
                "John",
                "Doe",
                "john.doe@example.com",
                "9876543210",
                "SecretPassword123",
                Gender.MALE,
                LocalDate.of(1990, 1, 1)
        );

        when(userRepository.existsByEmail(request.email())).thenReturn(true);

        assertThatThrownBy(() -> userService.createUser(request))
                .isInstanceOf(ResourceAlreadyExistsException.class)
                .hasMessageContaining("User already exists with email");

        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Create User - Short password throws ValidationException")
    void testCreateUser_ShortPassword() {
        CreateUserRequest request = new CreateUserRequest(
                "John",
                "Doe",
                "john.doe@example.com",
                "9876543210",
                "short", // Less than 8 chars
                Gender.MALE,
                LocalDate.of(1990, 1, 1)
        );

        assertThatThrownBy(() -> userService.createUser(request))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Password must be at least 8 characters");

        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Create User - Future DOB throws ValidationException")
    void testCreateUser_FutureDOB() {
        CreateUserRequest request = new CreateUserRequest(
                "John",
                "Doe",
                "john.doe@example.com",
                "9876543210",
                "SecretPassword123",
                Gender.MALE,
                LocalDate.now().plusDays(5)
        );

        assertThatThrownBy(() -> userService.createUser(request))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Date of birth cannot be a future date");
    }

    @Test
    @DisplayName("Get User by ID - Found and active")
    void testGetUserById_Success() {
        when(userRepository.findByUserIdAndStatusNot(userId, Status.DELETED)).thenReturn(Optional.of(user));
        when(userMapper.toResponse(user)).thenReturn(userResponse);

        UserResponse result = userService.getUserById(userId);

        assertThat(result).isNotNull();
        assertThat(result.userId()).isEqualTo(userId);
    }

    @Test
    @DisplayName("Get User by ID - Not Found or Deleted")
    void testGetUserById_NotFound() {
        when(userRepository.findByUserIdAndStatusNot(userId, Status.DELETED)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserById(userId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User not found with id");
    }

    @Test
    @DisplayName("Update User - Success")
    void testUpdateUser_Success() {
        UpdateUserRequest updateRequest = new UpdateUserRequest(
                "Johnny",
                "Doe",
                "johnny.doe@example.com",
                "9876543210",
                Gender.MALE,
                LocalDate.of(1990, 1, 1)
        );

        when(userRepository.findByUserIdAndStatusNot(userId, Status.DELETED)).thenReturn(Optional.of(user));
        when(userRepository.existsByEmailAndUserIdNot(updateRequest.email(), userId)).thenReturn(false);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toResponse(user)).thenReturn(userResponse);

        UserResponse result = userService.updateUser(userId, updateRequest);

        assertThat(result).isNotNull();
        verify(userMapper, times(1)).updateEntityFromDto(updateRequest, user);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    @DisplayName("Update User - Email in use by another user throws ResourceAlreadyExistsException")
    void testUpdateUser_EmailInUse() {
        UpdateUserRequest updateRequest = new UpdateUserRequest(
                "Johnny",
                "Doe",
                "already.taken@example.com",
                "9876543210",
                Gender.MALE,
                LocalDate.of(1990, 1, 1)
        );

        when(userRepository.findByUserIdAndStatusNot(userId, Status.DELETED)).thenReturn(Optional.of(user));
        when(userRepository.existsByEmailAndUserIdNot(updateRequest.email(), userId)).thenReturn(true);

        assertThatThrownBy(() -> userService.updateUser(userId, updateRequest))
                .isInstanceOf(ResourceAlreadyExistsException.class)
                .hasMessageContaining("Email already in use");

        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Update User Status - Success")
    void testUpdateUserStatus_Success() {
        UpdateUserStatusRequest statusRequest = new UpdateUserStatusRequest(Status.SUSPENDED);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toResponse(user)).thenReturn(userResponse);

        UserResponse result = userService.updateUserStatus(userId, statusRequest);

        assertThat(result).isNotNull();
        assertThat(user.getStatus()).isEqualTo(Status.SUSPENDED);
    }

    @Test
    @DisplayName("Soft Delete User - Success")
    void testDeleteUser_Success() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userService.deleteUser(userId);

        assertThat(user.getStatus()).isEqualTo(Status.DELETED);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    @DisplayName("Soft Delete User - Already deleted throws BusinessException")
    void testDeleteUser_AlreadyDeleted() {
        user.setStatus(Status.DELETED);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> userService.deleteUser(userId))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("User is already deleted");

        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Search Users - Page result returned")
    void testSearchUsers_Success() {
        UserSearchRequest searchRequest = new UserSearchRequest(
                "John",
                Status.ACTIVE,
                Gender.MALE,
                true,
                false,
                0,
                10,
                "firstName",
                SortDirection.ASC
        );

        when(userRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(user)));
        when(userMapper.toResponse(user)).thenReturn(userResponse);

        PageResponse<UserResponse> result = userService.searchUsers(searchRequest);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getTotalElements()).isEqualTo(1);
    }
}
