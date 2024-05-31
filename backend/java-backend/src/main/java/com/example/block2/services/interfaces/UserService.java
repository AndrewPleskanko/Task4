package com.example.block2.services.interfaces;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.block2.dto.GroupResponseDto;
import com.example.block2.dto.UserDto;
import com.example.block2.dto.UserFilterDto;
import com.example.block2.dto.UserReportDto;
import com.example.block2.dto.UserUploadResultDto;
import com.example.block2.entity.User;

public interface UserService {
    User createUser(UserDto userDto);

    List<UserDto> getAllUsers();

    User getUser(Long id);

    User updateUser(Long id, UserDto userDto);

    void deleteUser(Long id);

    GroupResponseDto<UserDto> listUsers(UserFilterDto filter);

    UserUploadResultDto uploadUsersFromFile(MultipartFile multipartFile) throws Exception;

    UserReportDto generateReport(UserFilterDto filter);
}
