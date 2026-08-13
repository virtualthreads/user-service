package com.aeropelican.userservice.service;

import com.aeropelican.userservice.dto.CreateRoleRequest;
import com.aeropelican.userservice.dto.RoleResponse;
import com.aeropelican.userservice.dto.UpdateRoleRequest;
import com.aeropelican.userservice.entity.Role;
import com.aeropelican.userservice.exception.ResourceAlreadyExistsException;
import com.aeropelican.userservice.exception.ResourceInUseException;
import com.aeropelican.userservice.exception.ResourceNotFoundException;
import com.aeropelican.userservice.mapper.RoleMapper;
import com.aeropelican.userservice.repository.RoleRepository;
import com.aeropelican.userservice.repository.UserRoleRepository;
import com.aeropelican.userservice.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

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
class RoleServiceImplTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserRoleRepository userRoleRepository;

    @Spy
    private RoleMapper roleMapper;

    @InjectMocks
    private RoleServiceImpl roleService;

    private UUID roleId;
    private Role roleEntity;

    @BeforeEach
    void setUp() {
        roleId = UUID.randomUUID();
        roleEntity = new Role(roleId, "ADMIN", "Administrator", LocalDateTime.now(), LocalDateTime.now());
    }

    @Test
    void createRole_Success() {
        CreateRoleRequest request = new CreateRoleRequest("ADMIN", "Administrator");

        when(roleRepository.existsByRoleName("ADMIN")).thenReturn(false);
        when(roleRepository.save(any(Role.class))).thenReturn(roleEntity);

        RoleResponse response = roleService.createRole(request);

        assertNotNull(response);
        assertEquals("ADMIN", response.roleName());
        verify(roleRepository).save(any(Role.class));
    }

    @Test
    void createRole_ThrowsResourceAlreadyExistsException_WhenRoleExists() {
        CreateRoleRequest request = new CreateRoleRequest("ADMIN", "Administrator");
        when(roleRepository.existsByRoleName("ADMIN")).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> roleService.createRole(request));
    }

    @Test
    void getRoleById_Success() {
        when(roleRepository.findById(roleId)).thenReturn(Optional.of(roleEntity));

        RoleResponse response = roleService.getRoleById(roleId);

        assertNotNull(response);
        assertEquals(roleId, response.roleId());
    }

    @Test
    void getRoleById_ThrowsResourceNotFoundException_WhenNotFound() {
        when(roleRepository.findById(roleId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> roleService.getRoleById(roleId));
    }

    @Test
    void deleteRole_Success() {
        when(roleRepository.findById(roleId)).thenReturn(Optional.of(roleEntity));
        when(userRoleRepository.existsByRole_RoleId(roleId)).thenReturn(false);

        roleService.deleteRole(roleId);

        verify(roleRepository).delete(roleEntity);
    }

    @Test
    void deleteRole_ThrowsResourceInUseException_WhenAssignedToUsers() {
        when(roleRepository.findById(roleId)).thenReturn(Optional.of(roleEntity));
        when(userRoleRepository.existsByRole_RoleId(roleId)).thenReturn(true);

        assertThrows(ResourceInUseException.class, () -> roleService.deleteRole(roleId));
    }
}
