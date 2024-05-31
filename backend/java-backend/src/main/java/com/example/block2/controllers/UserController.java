package com.example.block2.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.block2.dto.GroupResponseDto;
import com.example.block2.dto.UserDto;
import com.example.block2.dto.UserFilterDto;
import com.example.block2.dto.UserReportDto;
import com.example.block2.dto.UserUploadResultDto;
import com.example.block2.entity.User;
import com.example.block2.mapper.UserMapper;
import com.example.block2.services.interfaces.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

//NOPMD
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3050")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @Operation(summary = "Create a new user",
            description = "Creates a new user and returns the created user",
            responses = {
                    @ApiResponse(responseCode = "201", description = "User successfully created",
                            content = @Content(schema = @Schema(implementation = UserDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid UserDto input"),
                    @ApiResponse(responseCode = "500", description = "Server error")
            })
    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
        log.info("Received request to create user: {}", userDto);
        User user = userService.createUser(userDto);

        return new ResponseEntity<>(userMapper.toSafeDto(user), HttpStatus.CREATED);
    }

    @Operation(summary = "Get a user",
            description = "Returns a user by id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User successfully retrieved",
                            content = @Content(schema = @Schema(implementation = UserDto.class))),
                    @ApiResponse(responseCode = "404", description = "User not found"),
                    @ApiResponse(responseCode = "500", description = "Server error")
            })
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        log.info("Received request to get user with id: {}", id);
        User user = userService.getUser(id);

        return ResponseEntity.ok(userMapper.toSafeDto(user));
    }

    @Operation(summary = "Update a user",
            description = "Updates a user and returns the updated user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User successfully updated",
                            content = @Content(schema = @Schema(implementation = UserDto.class))),
                    @ApiResponse(responseCode = "404", description = "User not found"),
                    @ApiResponse(responseCode = "400", description = "Invalid UserDto input"),
                    @ApiResponse(responseCode = "500", description = "Server error")
            })
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @Valid @RequestBody UserDto userDto) {
        log.info("Received request to update user with id: {}", id);
        User user = userService.updateUser(id, userDto);

        return ResponseEntity.ok(userMapper.toSafeDto(user));
    }

    @Operation(summary = "Delete a user",
            description = "Deletes a user",
            responses = {
                    @ApiResponse(responseCode = "204", description = "User successfully deleted"),
                    @ApiResponse(responseCode = "404", description = "User not found"),
                    @ApiResponse(responseCode = "500", description = "Server error")
            })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.info("Received request to delete user with id: {}", id);
        userService.deleteUser(id);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "List users",
            description = "Returns a list of users based on a filter",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Users successfully listed",
                            content = @Content(schema = @Schema(implementation = GroupResponseDto.class))),
                    @ApiResponse(responseCode = "500", description = "Server error")
            })
    @PostMapping("/_list")
    public ResponseEntity<GroupResponseDto<UserDto>> list(@RequestBody UserFilterDto filter) {
        log.info("Received request to list users with filter: {}", filter);

        return ResponseEntity.ok(userService.listUsers(filter));
    }

    @Operation(summary = "Generate report",
            description = "Generates a report based on a filter",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Report successfully generated",
                            content = @Content(schema = @Schema(implementation = byte[].class))),
                    @ApiResponse(responseCode = "500", description = "Server error")
            })
    @PostMapping("/_report")
    public ResponseEntity<byte[]> generateReport(@RequestBody UserFilterDto filter) {
        log.info("Received request to generate report for filter: {}", filter);
        UserReportDto reportDto = userService.generateReport(filter);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + reportDto.getFileName())
                .body(reportDto.getContent());
    }

    @Operation(summary = "Upload file",
            description = "Uploads a file and returns statistics",
            responses = {
                    @ApiResponse(responseCode = "200", description = "File successfully uploaded",
                            content = @Content(schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "500", description = "Server error")
            })
    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile multipartFile) throws Exception {
        log.info("Received request to upload file");
        UserUploadResultDto result = userService.uploadUsersFromFile(multipartFile);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
