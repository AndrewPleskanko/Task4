package com.example.block2.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.block2.dto.RoleDto;
import com.example.block2.entity.Role;
import com.example.block2.mapper.RoleMapper;
import com.example.block2.services.interfaces.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

//NOPMD
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
@Slf4j
@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
@Tag(name = "Role Management System")
public class RoleController {

    private final RoleService roleService;
    private final RoleMapper roleMapper;

    @PostMapping
    @Operation(summary = "Create a new name",
            description = "Creates a new name and returns the created name",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Role successfully created",
                            content = @Content(schema = @Schema(implementation = RoleDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid RoleDto input"),
                    @ApiResponse(responseCode = "500", description = "Server error")
            })
    public ResponseEntity<RoleDto> createRole(@Valid @RequestBody RoleDto role) {
        log.info("Received request to create a name");
        Role createdRole = roleService.createRole(role);

        return new ResponseEntity<>(roleMapper.toDto(createdRole), HttpStatus.CREATED);
    }

    @Operation(summary = "Get all roles",
            description = "Returns a list of all roles",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Roles successfully retrieved",
                            content = @Content(schema = @Schema(implementation = RoleDto.class))),
                    @ApiResponse(responseCode = "500", description = "Server error")
            })
    @GetMapping
    public ResponseEntity<List<RoleDto>> getRoles() {
        log.info("Received request to get all roles");
        List<Role> roles = roleService.getAllRoles();

        return ResponseEntity.ok(roles.stream()
                .map(roleMapper::toDto)
                .collect(Collectors.toList()));
    }

    @Operation(summary = "Update a name",
            description = "Updates a name and returns the updated name",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Role successfully updated",
                            content = @Content(schema = @Schema(implementation = RoleDto.class))),
                    @ApiResponse(responseCode = "404", description = "Role not found"),
                    @ApiResponse(responseCode = "400", description = "Invalid RoleDto input"),
                    @ApiResponse(responseCode = "500", description = "Server error")
            })
    @PutMapping("/{id}")
    public ResponseEntity<RoleDto> updateRole(@PathVariable Long id, @Valid @RequestBody RoleDto role) {
        log.info("Received request to update name with id: {}", id);
        Role updatedRole = roleService.updateRole(id, role);

        return ResponseEntity.ok(roleMapper.toDto(updatedRole));
    }

    @Operation(summary = "Delete a name",
            description = "Deletes a name",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Role successfully deleted"),
                    @ApiResponse(responseCode = "404", description = "Role not found"),
                    @ApiResponse(responseCode = "500", description = "Server error")
            })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        log.info("Received request to delete name with id: {}", id);
        roleService.deleteRole(id);

        return ResponseEntity.noContent().build();
    }
}
