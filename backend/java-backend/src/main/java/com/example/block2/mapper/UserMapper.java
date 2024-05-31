package com.example.block2.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.block2.dto.UserDto;
import com.example.block2.entity.User;

//NOPMD
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "username", target = "username")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "role", target = "role")
    UserDto toDto(User user);

    @Mapping(source = "username", target = "username")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "role", target = "role")
    User toEntity(UserDto userDto);

    @Mapping(source = "username", target = "username")
    @Mapping(source = "password", target = "password",ignore = true)
    @Mapping(source = "email", target = "email")
    @Mapping(source = "role", target = "role")
    UserDto toSafeDto(User user);
}