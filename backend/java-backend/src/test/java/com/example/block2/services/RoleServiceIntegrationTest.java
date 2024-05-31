package com.example.block2.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.block2.dto.RoleDto;
import com.example.block2.entity.Role;
import com.example.block2.exceptions.EntityNotFoundException;
import com.example.block2.repositories.RoleRepository;
import com.example.block2.repositories.UserRepository;
import com.example.block2.services.interfaces.RoleService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RoleServiceIntegrationTest extends BaseServiceTest {

    @Autowired
    private RoleService roleService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    private RoleDto roleDto;

    @BeforeEach
    public void setUp() {

        userRepository.deleteAll();
        roleRepository.deleteAll();
        roleDto = new RoleDto();
        roleDto.setName("User");
    }

    @Test
    public void createRole_createsNewRole_returnsCreatedRole() {
        // When
        Role createdRole = roleService.createRole(roleDto);

        // Then
        assertEquals(roleDto.getName(), createdRole.getName());
    }

    @Test
    public void getRole_createsAndGetsRole_returnsRole() {
        // Given
        Role createdRole = roleService.createRole(roleDto);
        Role retrievedRole = roleService.getRole(createdRole.getId());

        // Then
        assertEquals(createdRole.getName(), retrievedRole.getName());
    }

    @Test
    public void updateRole_updatesRole_returnsUpdatedRole() {
        // Given
        Role createdRole = roleService.createRole(roleDto);
        roleDto.setName("ROLE_UPDATED");

        // When
        Role updatedRole = roleService.updateRole(createdRole.getId(), roleDto);

        // Then
        assertEquals(roleDto.getName(), updatedRole.getName());
    }

    @Test
    public void deleteRole_deletesRole_throwsRoleNotFoundException() {
        // Given
        Role createdRole = roleService.createRole(roleDto);

        // When
        roleService.deleteRole(createdRole.getId());

        // Then
        assertThrows(EntityNotFoundException.class, () -> roleService.getRole(createdRole.getId()));
    }

    @Test
    public void getAllRoles_createsAndGetsAllRoles_returnsRoles() {
        // Given
        Role createdRole = roleService.createRole(roleDto);

        // When
        List<Role> roles = roleService.getAllRoles();

        // Then
        assertEquals(1, roles.size());
        assertEquals(createdRole, roles.get(0));
    }

    @Test
    public void getRole_withInvalidId_throwsRoleNotFoundException() {
        // When & Then
        assertThrows(EntityNotFoundException.class, () -> roleService.getRole(-1L));
    }

    @Test
    public void updateRole_withInvalidId_throwsRuntimeException() {
        // Given
        RoleDto roleDto = new RoleDto();
        roleDto.setName("Test Role");

        // When & Then
        assertThrows(RuntimeException.class, () -> roleService.updateRole(-1L, roleDto));
    }

    @Test
    public void deleteRole_withInvalidId_throwsRoleDeleteException() {
        // Given
        RoleDto roleDto = new RoleDto();
        roleDto.setName("ROLE_TEST");
        Role createdRole = roleService.createRole(roleDto);

        // When
        roleService.deleteRole(createdRole.getId());

        // Then
        assertThrows(EntityNotFoundException.class, () -> roleService.getRole(createdRole.getId()));
    }
}