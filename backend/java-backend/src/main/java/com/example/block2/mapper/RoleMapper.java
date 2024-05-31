package com.example.block2.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.block2.dto.RoleDto;
import com.example.block2.entity.Role;

//NOPMD
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Mapping(source = "name", target = "name")
    RoleDto toDto(Role role);

    @Mapping(source = "name", target = "name")
    Role toEntity(RoleDto roleDto);
}