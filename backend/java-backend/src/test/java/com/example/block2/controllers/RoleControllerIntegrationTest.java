package com.example.block2.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;

import com.example.block2.dto.RoleDto;
import com.example.block2.entity.Role;
import com.example.block2.services.BaseServiceTest;
import com.example.block2.services.interfaces.RoleService;
import com.example.block2.utils.RoleTestUtils;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RoleControllerIntegrationTest extends BaseServiceTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private RoleService roleService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setUp() {
        clearDatabase();
    }

    @AfterEach
    public void tearDown() {
        clearDatabase();
    }

    private void clearDatabase() {
        jdbcTemplate.execute("DELETE FROM app_user");
        jdbcTemplate.execute("DELETE FROM role");
    }

    @Test
    public void createRole_createsNewRole_returnsCreatedRole() {
        // Given
        RoleDto roleDto = new RoleDto();
        roleDto.setName("ROLE_TEST_" + UUID.randomUUID());

        // When
        ResponseEntity<RoleDto> response = restTemplate.postForEntity("http://localhost:" + port + "/api/v1/roles", roleDto, RoleDto.class);

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(roleDto.getName(), response.getBody().getName());
    }

    @Test
    public void getRoles_createsAndGetsRoles_returnsRoles() {
        // Given
        RoleDto roleDto = RoleTestUtils.createRoleDto(3, "ROLE_TEST_2");
        roleService.createRole(roleDto);

        // When
        ResponseEntity<RoleDto[]> response = restTemplate.getForEntity("http://localhost:" + port + "/api/v1/roles", RoleDto[].class);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void updateRole_UpdatesRoleFields_ReturnsUpdatedRole() {
        // Given
        RoleDto roleDto = new RoleDto();
        roleDto.setName("ROLE_UPDATED_" + UUID.randomUUID());

        RoleDto roleToBeUpdated = new RoleDto();
        roleToBeUpdated.setName("ROLE_TO_BE_UPDATED_" + UUID.randomUUID());
        Role createdRole = roleService.createRole(roleToBeUpdated);

        HttpEntity<RoleDto> requestEntity = new HttpEntity<>(roleDto);
        ResponseEntity<RoleDto> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/v1/roles/" + createdRole.getId(), HttpMethod.PUT, requestEntity, RoleDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(roleDto.getName(), response.getBody().getName());
    }

    @Test
    public void deleteRole_DeletesExistingRole_ReturnsNoContent() {
        // Given
        RoleDto roleDto = new RoleDto();
        roleDto.setName("ROLE_TO_BE_DELETED");
        Role createdRole = roleService.createRole(roleDto);

        // When
        ResponseEntity<Void> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/v1/roles/" + createdRole.getId(), HttpMethod.DELETE, null, Void.class);

        // Then
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}