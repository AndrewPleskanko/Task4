package com.example.block2.utils;

import com.example.block2.dto.RoleDto;
import com.example.block2.dto.UserDto;

public class UserTestUtils {
    public static UserDto createUserDto(String username, String password, RoleDto role, String email) {
        UserDto userDto = new UserDto();
        userDto.setUsername(username);
        userDto.setPassword(password);
        userDto.setRole(role);
        userDto.setEmail(email);
        return userDto;
    }
}
