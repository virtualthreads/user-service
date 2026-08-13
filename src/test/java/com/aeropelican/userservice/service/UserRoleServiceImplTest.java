package com.aeropelican.userservice.service;

import com.aeropelican.userservice.dto.AssignRoleRequest;
import com.aeropelican.userservice.dto.RoleResponse;
import com.aeropelican.userservice.entity.Gender;
import com.aeropelican.userservice.entity.Role;
import com.aeropelican.userservice.entity.Status;
import com.aeropelican.userservice.entity.User;
import com.aeropelican.userservice.entity.UserRole;
import com.aeropelican.userservice.exception.ResourceAlreadyExistsException;
import com.aeropelican.userservice.exception.ResourceNotFoundException;
import com.aeropelican.userservice.mapper.RoleMapper;
import com.aeropelican.userservice.repository.RoleRepository;
import com.aeropelican.userservice.repository.UserRepository;
import com.aeropelican.userservice.repository.UserRoleRepository;
import com.aeropelican.userservice.service.impl.UserRoleServiceImpl;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserRoleServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserRoleRepository userRoleRepository;

    @Spy
    private RoleMapper roleMapper;

    @InjectMocks
    private UserRoleServiceImpl userRoleService;

    private UUID userId;
    private UUID roleId;
    private User user;
    private Role role;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        roleId = UUID.randomUUID();

        user = new User(userId, "John", "Doe", "john@example.com", "9876543210", "pass",
                Gender.MALE, LocalDate.of(1995, 5, 20), true, true, Status.ACTIVE, LocalDateTime.now(), LocalDateTime.now());

        role = new Role(roleId, "ADMIN", "Administrator", LocalDateTime.now(), LocalDateTime.now());
    }

    @Test
    void assignRole_Success() {
        AssignRoleRequest request = new AssignRoleRequest(roleId);

        when(userRepository.findByUserIdAndStatusNot(userId, Status.DELETED)).thenReturn(Optional.of(user));
        when(roleRepository.findById(roleId)).thenReturn(Optional.of(role));
        when(userRoleRepository.existsByUser_UserIdAndRole_RoleId(userId, roleId)).thenReturn(false);

        RoleResponse response = userRoleService.assignRole(userId, request);

        assertNotNull(response);
        assertEquals("ADMIN", response.roleName());
        verify(userRoleRepository).save(any(UserRole.class));
    }

    @Test
    void assignRole_ThrowsResourceAlreadyExistsException_WhenMappingExists() {
        AssignRoleRequest request = new AssignRoleRequest(roleId);

        when(userRepository.findByUserIdAndStatusNot(userId, Status.DELETED)).thenReturn(Optional.of(user));
        when(roleRepository.findById(roleId)).thenReturn(Optional.of(role));
        when(userRoleRepository.existsByUser_UserIdAndRole_RoleId(userId, roleId)).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> userRoleService.assignRole(userId, request));
    }

    @Test
    void getUserRoles_Success() {
        UserRole userRole = new UserRole(UUID.randomUUID(), user, role, LocalDateTime.now());

        when(userRepository.existsById(userId)).thenReturn(true);
        when(userRoleRepository.findByUser_UserId(userId)).thenReturn(List.of(userRole));

        List<RoleResponse> result = userRoleService.getUserRoles(userId);

        assertEquals(1, result.size());
        assertEquals("ADMIN", result.get(0).roleName());
    }

    @Test
    void removeRole_Success() {
        UserRole userRole = new UserRole(UUID.randomUUID(), user, role, LocalDateTime.now());

        when(userRepository.existsById(userId)).thenReturn(true);
        when(roleRepository.existsById(roleId)).thenReturn(true);
        when(userRoleRepository.findByUser_UserIdAndRole_RoleId(userId, roleId)).thenReturn(Optional.of(userRole));

        userRoleService.removeRole(userId, roleId);

        verify(userRoleRepository).delete(userRole);
    }
}
