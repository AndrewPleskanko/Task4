package com.example.block2.services.interfaces;

import java.util.List;

import com.example.block2.dto.RoleDto;
import com.example.block2.entity.Role;

public interface RoleService {
    Role createRole(RoleDto roleDto);

    List<Role> getAllRoles();

    Role getRole(Long id);

    Role updateRole(Long id, RoleDto roleDto);

    void deleteRole(Long id);

    Role getRoleByName(String roleName);

    void deleteAllRoles();
}
