package com.example.block2.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.block2.dto.GroupResponseDto;
import com.example.block2.dto.RoleDto;
import com.example.block2.dto.UserDto;
import com.example.block2.dto.UserFilterDto;
import com.example.block2.entity.Role;
import com.example.block2.entity.User;
import com.example.block2.exceptions.EntityNotFoundException;
import com.example.block2.mapper.RoleMapper;
import com.example.block2.mapper.UserMapper;
import com.example.block2.repositories.RoleRepository;
import com.example.block2.repositories.UserRepository;
import com.example.block2.utils.RoleTestUtils;
import com.example.block2.utils.UserTestUtils;

@SpringBootTest
public class UserServiceIntegrationTest extends BaseServiceTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private UserMapper userMapper;

    RoleDto roleUserDto;

    @BeforeEach
    void setUp() {
        //Given
        userRepository.deleteAll();
        roleRepository.deleteAll();
        roleUserDto = RoleTestUtils.createRoleDto(1, "ROLE_USER");
        RoleDto roleAdminDto = RoleTestUtils.createRoleDto(2, "ROLE_ADMIN");
        Role roleUser = roleMapper.toEntity(roleUserDto);
        Role roleAdmin = roleMapper.toEntity(roleAdminDto);
        // Save roles in the database
        roleUser = roleRepository.save(roleUser);
        roleAdmin = roleRepository.save(roleAdmin);
        // Save users in the database
        userRepository.save(new User("test1", "test123", "presF@gmail.com", roleUser, "1234567890", 25));
        userRepository.save(new User("test2", "test234", "test2@gmail.com", roleAdmin, "1234567890", 25));
        UserDto userDto1 = UserTestUtils.createUserDto("john", "123", roleMapper.toDto(roleUser), "testUser@gmail.com");
        UserDto userDto2 = UserTestUtils.createUserDto("admin", "admin", roleAdminDto, "testAdmin@gmail.com");
        userService.createUser(userDto1);
        userService.createUser(userDto2);
    }

    @Test
    public void createUser_createsNewUser_returnsCreatedUser() {
        // Given
        UserDto userDto = new UserDto(null, "testCreateUser",
                "testCreatePassword", "testCreateUser@gmail.com", roleUserDto, "1234567890", 25);

        // When
        User createdUserDto = userService.createUser(userDto);

        // Then
        assertNotNull(createdUserDto.getId());
        assertEquals(userDto.getUsername(), createdUserDto.getUsername());
        assertEquals(userDto.getEmail(), createdUserDto.getEmail());
        assertEquals(userDto.getRole().getName(), createdUserDto.getRole().getName());
    }

    @Test
    public void getAllUsers_createsAndGetsUsers_returnsUsers() {
        // Given
        RoleDto roleDto = new RoleDto(1L, "ROLE_USER_GetAllUsers");
        Role save = roleRepository.save(roleMapper.toEntity(roleDto));
        User user1 = new User("testUser1", "testPassword1",
                "testUser1@gmail.com", roleMapper.toEntity(roleDto), "1234567890", 25);
        User user2 = new User("testUser2", "testPassword2",
                "testUser2@gmail.com", save, "1234567890", 25);

        // When
        List<UserDto> users = userService.getAllUsers();

        // Then
        assertEquals(4, users.size());
    }


    @Test
    void getUserById_getsUserById_returnsUser() {
        // Given
        RoleDto roleDto = new RoleDto(1L, "ROLE_USER_GetAllUsers");
        Role save = roleRepository.save(roleMapper.toEntity(roleDto));
        User user = new User("testUser", "testPassword", "testUser@gmail.com", save, "1234567890", 25);
        User savedUser = userRepository.save(user);
        Long id = savedUser.getId();

        // When
        User result = userService.getUser(id);

        // Then
        assertEquals(savedUser.getId(), result.getId());
        assertEquals(savedUser.getUsername(), result.getUsername());
        assertEquals(savedUser.getEmail(), result.getEmail());
        assertEquals(savedUser.getRole().getId(), result.getRole().getId());
    }

    @Test
    public void updateUser_updatesUser_returnsUpdatedUser() {
        // Given
        RoleDto roleDto = new RoleDto(1L, "ROLE_USER_Update");
        roleRepository.save(roleMapper.toEntity(roleDto));
        UserDto userDto = new UserDto(null, "testUpdateUser",
                "testUpdatePassword", "testUpdateUser@gmail.com", roleDto, "1234567890", 25);
        User createdUser = userService.createUser(userDto);

        UserDto updatedUserDto = new UserDto(createdUser.getId(), "updatedUsername",
                "updatedPassword", createdUser.getEmail(), roleDto, createdUser.getPhone(), createdUser.getAge());

        // When
        User updatedUser = userService.updateUser(createdUser.getId(), updatedUserDto);

        // Then
        assertEquals(createdUser.getId(), updatedUser.getId());
        assertEquals(updatedUserDto.getUsername(), updatedUser.getUsername());
    }

    @Test
    public void deleteUser_deletesUser_userDoesNotExist() {
        // Given
        RoleDto roleDto = new RoleDto(2L, "ROLE_Delete");
        Role save = roleRepository.save(roleMapper.toEntity(roleDto));
        User user = new User("testUser", "testPassword", "testUser@gmail.com", save, "1234567890", 25);
        UserDto userDto = new UserDto(null, user.getUsername(), user.getPassword(),
                user.getEmail(), roleDto, user.getPhone(), user.getAge());
        User createdUserDto = userService.createUser(userDto);

        // When
        userService.deleteUser(createdUserDto.getId());

        // Then
        assertFalse(userRepository.existsById(createdUserDto.getId()));
    }

    @Test
    public void listUsers_listsUsers_returnsUsers() {
        // Given
        Role role = roleRepository.save(new Role("USER"));
        userRepository.save(new User("testUser1345444444442",
                "testPassword1", "testUser1@gmail.com", role, "1234567890", 25));
        userRepository.save(new User("testUser22467358467534",
                "testPassword2", "testUser2@gmail.com", role, "1234567890", 25));
        UserFilterDto filter = new UserFilterDto();
        filter.setUsername("testUser");
        filter.setPage(0);
        Pageable pageable = PageRequest.of(0, 5);

        // When
        GroupResponseDto<UserDto> users = userService.listUsers(filter);

        // Then
        assertEquals(2, users.getContent().size());
    }

    @Test
    public void createUser_withNullInput_throwsRuntimeException() {
        // When & Then
        assertThrows(RuntimeException.class, () -> userService.createUser(null));
    }

    @Test
    public void getUser_withInvalidId_throwsUserNotFoundException() {
        // When & Then
        assertThrows(EntityNotFoundException.class, () -> userService.getUser(-1L));
    }

    @Test
    public void updateUser_withInvalidId_throwsRuntimeException() {
        // Given
        UserDto userDto = new UserDto();
        userDto.setUsername("Test User");

        // When & Then
        assertThrows(RuntimeException.class, () -> userService.updateUser(-1L, userDto));
    }

    @Test
    public void deleteUser_withInvalidId_throwsUserNotFoundException() {
        // When & Then
        assertThrows(EntityNotFoundException.class, () -> userService.deleteUser(-1L));
    }
}