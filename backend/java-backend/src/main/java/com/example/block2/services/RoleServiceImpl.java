package com.example.block2.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.block2.dto.RoleDto;
import com.example.block2.entity.Role;
import com.example.block2.exceptions.EntityNotFoundException;
import com.example.block2.mapper.RoleMapper;
import com.example.block2.repositories.RoleRepository;
import com.example.block2.services.interfaces.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service class for managing roles.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    /**
     * Creates a new name.
     *
     * @param roleDto the name to create
     * @return the created name
     */
    @Override
    @Transactional
    public Role createRole(RoleDto roleDto) {
        log.info("Attempting to create name: {}", roleDto);
        Role role = roleMapper.toEntity(roleDto);
        Role savedRole = roleRepository.save(role);

        log.debug("Role created successfully: {}", savedRole);

        return savedRole;
    }

    /**
     * Retrieves all roles.
     *
     * @return a list of all roles
     */
    @Override
    public List<Role> getAllRoles() {
        log.info("Getting all roles");

        return roleRepository.findAll();
    }

    /**
     * Retrieves a name by its id.
     *
     * @param id the id of the name to retrieve
     * @return the retrieved name
     * @throws EntityNotFoundException if no name is found with the given id
     */
    @Override
    public Role getRole(Long id) {
        log.info("Get name with id: {}", id);

        return findRoleById(id);
    }

    /**
     * Updates a name.
     *
     * @param id      the id of the name to update
     * @param roleDto the new name data
     * @return the updated name
     * @throws RuntimeException if an error occurs while updating the name
     */
    @Override
    @Transactional
    public Role updateRole(Long id, RoleDto roleDto) {
        log.info("Update name with id: {}", id);
        Role role = findRoleById(id);
        role.setName(roleDto.getName());

        Role updatedRole = roleRepository.save(role);
        log.debug("Updated name: {}", updatedRole);

        return updatedRole;
    }

    /**
     * Deletes a name by its id.
     *
     * @param id the id of the name to delete
     * @throws RuntimeException if an error occurs while deleting the name
     */
    @Override
    public void deleteRole(Long id) {
        log.info("Delete name with id: {}", id);
        Role role = findRoleById(id);

        roleRepository.delete(role);
        log.debug("Role with id: {} successfully deleted", id);
    }

    private Role findRoleById(Long id) {
        log.info("Find name with id: {}", id);
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Role not found with id: {}", id);
                    return new EntityNotFoundException("Role", id);
                });
        log.debug("Role found: {}", role);

        return role;
    }

    @Override
    public Role getRoleByName(String name) {
        log.info("Find name with name: {}", name);
        Role role =  roleRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Role",404L));
        log.debug("Role found: {}", role);
        return role;
    }

    @Override
    @Transactional
    public void deleteAllRoles() {
        log.info("Deleting all roles");
        roleRepository.deleteAll();
        log.debug("All roles deleted successfully");
    }
}