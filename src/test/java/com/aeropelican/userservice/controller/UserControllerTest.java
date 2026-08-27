package com.aeropelican.userservice.controller;

import com.aeropelican.userservice.dto.SortDirection;
import com.aeropelican.userservice.dto.request.CreateUserRequest;
import com.aeropelican.userservice.dto.request.UpdateUserRequest;
import com.aeropelican.userservice.dto.request.UpdateUserStatusRequest;
import com.aeropelican.userservice.dto.request.UserSearchRequest;
import com.aeropelican.userservice.dto.response.PageResponse;
import com.aeropelican.userservice.dto.response.UserResponse;
import com.aeropelican.userservice.entity.Gender;
import com.aeropelican.userservice.entity.Status;
import com.aeropelican.userservice.exception.BusinessException;
import com.aeropelican.userservice.exception.GlobalExceptionHandler;
import com.aeropelican.userservice.exception.ResourceAlreadyExistsException;
import com.aeropelican.userservice.exception.ResourceNotFoundException;
import com.aeropelican.userservice.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private UUID userId;
    private UserResponse sampleUserResponse;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        userId = UUID.randomUUID();
        sampleUserResponse = new UserResponse(
                userId,
                "Alex",
                "Mercer",
                "alex.mercer@example.com",
                "9876543210",
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
    @DisplayName("POST /api/v1/users - Successfully register user")
    void testRegisterUser_Success() throws Exception {
        CreateUserRequest request = new CreateUserRequest(
                "Alex",
                "Mercer",
                "alex.mercer@example.com",
                "9876543210",
                "StrongPass123!",
                Gender.MALE,
                LocalDate.of(1995, 5, 20)
        );

        when(userService.createUser(any(CreateUserRequest.class))).thenReturn(sampleUserResponse);

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("User registered successfully"))
                .andExpect(jsonPath("$.data.userId").value(userId.toString()))
                .andExpect(jsonPath("$.data.email").value("alex.mercer@example.com"))
                .andExpect(jsonPath("$.data.status").value("ACTIVE"));

        verify(userService, times(1)).createUser(any(CreateUserRequest.class));
    }

    @Test
    @DisplayName("POST /api/v1/users - Validation Failure: Missing mandatory fields")
    void testRegisterUser_ValidationFailure() throws Exception {
        CreateUserRequest invalidRequest = new CreateUserRequest(
                "", // Blank first name
                "Mercer",
                "invalid-email", // Invalid email format
                "9876543210",
                "", // Blank password
                Gender.MALE,
                LocalDate.now().plusDays(1) // Future DOB
        );

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.errorCode").value("VALIDATION_FAILED"))
                .andExpect(jsonPath("$.error.fieldErrors", hasSize(greaterThanOrEqualTo(1))));

        verify(userService, never()).createUser(any());
    }

    @Test
    @DisplayName("POST /api/v1/users - Conflict: Email already exists")
    void testRegisterUser_EmailAlreadyExists() throws Exception {
        CreateUserRequest request = new CreateUserRequest(
                "Alex",
                "Mercer",
                "alex.mercer@example.com",
                "9876543210",
                "StrongPass123!",
                Gender.MALE,
                LocalDate.of(1995, 5, 20)
        );

        when(userService.createUser(any(CreateUserRequest.class)))
                .thenThrow(new ResourceAlreadyExistsException("User already exists with email: " + request.email()));

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.errorCode").value("RESOURCE_CONFLICT"));
    }

    @Test
    @DisplayName("GET /api/v1/users/{userId} - Successfully get user by ID")
    void testGetUserById_Success() throws Exception {
        when(userService.getUserById(userId)).thenReturn(sampleUserResponse);

        mockMvc.perform(get("/api/v1/users/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.userId").value(userId.toString()))
                .andExpect(jsonPath("$.data.firstName").value("Alex"));

        verify(userService, times(1)).getUserById(userId);
    }

    @Test
    @DisplayName("GET /api/v1/users/{userId} - User Not Found")
    void testGetUserById_NotFound() throws Exception {
        when(userService.getUserById(userId))
                .thenThrow(new ResourceNotFoundException("User not found with id: " + userId));

        mockMvc.perform(get("/api/v1/users/{userId}", userId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.errorCode").value("RESOURCE_NOT_FOUND"));
    }

    @Test
    @DisplayName("GET /api/v1/users/{userId} - Invalid UUID format")
    void testGetUserById_InvalidUUID() throws Exception {
        mockMvc.perform(get("/api/v1/users/{userId}", "not-a-valid-uuid"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.errorCode").value("BAD_REQUEST"));
    }

    @Test
    @DisplayName("POST /api/v1/users/search - Successfully search users")
    void testSearchUsers_Success() throws Exception {
        UserSearchRequest searchRequest = new UserSearchRequest(
                "Alex",
                Status.ACTIVE,
                Gender.MALE,
                true,
                true,
                0,
                10,
                "firstName",
                SortDirection.ASC
        );

        PageResponse<UserResponse> pageResponse = PageResponse.<UserResponse>builder()
                .content(List.of(sampleUserResponse))
                .page(0)
                .size(10)
                .totalElements(1)
                .totalPages(1)
                .first(true)
                .last(true)
                .build();

        when(userService.searchUsers(any(UserSearchRequest.class))).thenReturn(pageResponse);

        mockMvc.perform(post("/api/v1/users/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(searchRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content", hasSize(1)))
                .andExpect(jsonPath("$.data.totalElements").value(1))
                .andExpect(jsonPath("$.data.content[0].email").value("alex.mercer@example.com"));

        verify(userService, times(1)).searchUsers(any(UserSearchRequest.class));
    }

    @Test
    @DisplayName("PUT /api/v1/users/{userId} - Successfully update user")
    void testUpdateUser_Success() throws Exception {
        UpdateUserRequest updateRequest = new UpdateUserRequest(
                "Alexander",
                "Mercer",
                "alex.new@example.com",
                "9876543219",
                Gender.MALE,
                LocalDate.of(1995, 5, 20)
        );

        UserResponse updatedResponse = new UserResponse(
                userId,
                "Alexander",
                "Mercer",
                "alex.new@example.com",
                "9876543219",
                Gender.MALE,
                LocalDate.of(1995, 5, 20),
                true,
                true,
                Status.ACTIVE,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(userService.updateUser(eq(userId), any(UpdateUserRequest.class))).thenReturn(updatedResponse);

        mockMvc.perform(put("/api/v1/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("User updated successfully"))
                .andExpect(jsonPath("$.data.firstName").value("Alexander"))
                .andExpect(jsonPath("$.data.email").value("alex.new@example.com"));

        verify(userService, times(1)).updateUser(eq(userId), any(UpdateUserRequest.class));
    }

    @Test
    @DisplayName("PATCH /api/v1/users/{userId}/status - Successfully update status")
    void testUpdateUserStatus_Success() throws Exception {
        UpdateUserStatusRequest statusRequest = new UpdateUserStatusRequest(Status.LOCKED);

        UserResponse lockedResponse = new UserResponse(
                userId,
                "Alex",
                "Mercer",
                "alex.mercer@example.com",
                "9876543210",
                Gender.MALE,
                LocalDate.of(1995, 5, 20),
                true,
                true,
                Status.LOCKED,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(userService.updateUserStatus(eq(userId), any(UpdateUserStatusRequest.class))).thenReturn(lockedResponse);

        mockMvc.perform(patch("/api/v1/users/{userId}/status", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(statusRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("User status updated successfully"))
                .andExpect(jsonPath("$.data.status").value("LOCKED"));

        verify(userService, times(1)).updateUserStatus(eq(userId), any(UpdateUserStatusRequest.class));
    }

    @Test
    @DisplayName("DELETE /api/v1/users/{userId} - Successfully soft-delete user")
    void testDeleteUser_Success() throws Exception {
        doNothing().when(userService).deleteUser(userId);

        mockMvc.perform(delete("/api/v1/users/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("User deleted successfully"));

        verify(userService, times(1)).deleteUser(userId);
    }

    @Test
    @DisplayName("DELETE /api/v1/users/{userId} - Fail when already deleted")
    void testDeleteUser_AlreadyDeleted() throws Exception {
        doThrow(new BusinessException("User is already deleted")).when(userService).deleteUser(userId);

        mockMvc.perform(delete("/api/v1/users/{userId}", userId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.errorCode").value("BUSINESS_EXCEPTION"));

        verify(userService, times(1)).deleteUser(userId);
    }
}
