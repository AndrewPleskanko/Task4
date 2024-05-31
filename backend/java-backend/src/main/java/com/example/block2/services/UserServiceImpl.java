package com.example.block2.services;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.block2.dto.GroupResponseDto;
import com.example.block2.dto.UserDto;
import com.example.block2.dto.UserFilterDto;
import com.example.block2.dto.UserReportDto;
import com.example.block2.dto.UserUploadResultDto;
import com.example.block2.entity.Role;
import com.example.block2.entity.User;
import com.example.block2.enums.UserReportType;
import com.example.block2.exceptions.EntityNotFoundException;
import com.example.block2.mapper.UserMapper;
import com.example.block2.repositories.UserRepository;
import com.example.block2.services.interfaces.RoleService;
import com.example.block2.services.interfaces.UserService;
import com.example.block2.services.userimport.UserReportStrategy;
import com.example.block2.services.userimport.UserReportStrategyFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.criteria.Predicate;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service class for managing users.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final UserReportStrategyFactory reportStrategyFactory;
    private final ObjectMapper objectMapper;

    /**
     * Creates a new user.
     *
     * @param userDto the user data transfer object
     * @return the created user data transfer object
     */
    @Override
    @Transactional
    public User createUser(UserDto userDto) {
        log.info("Starting createUser method with userDto: {}", userDto);
        User user = userMapper.toEntity(userDto);
        String userPassword = user.getPassword();
        String encodedPassword = passwordEncoder.encode(userPassword);
        user.setPassword(encodedPassword);
        log.info("Starting createUser method with userDto: {}", userDto);
        String roleName = userDto.getRole().getName();
        Role role = roleService.getRoleByName(roleName);
        user.setRole(role);

        User savedUser = userRepository.save(user);
        log.debug("Saved user: {}", savedUser);

        return savedUser;
    }

    /**
     * Retrieves a user by its id.
     *
     * @param id the id of the user to retrieve
     * @return the retrieved user
     * @throws EntityNotFoundException if no user is found with the given id
     */
    @Override
    public User getUser(Long id) {
        log.info("Reading user by id: {}", id);
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new EntityNotFoundException("User", id);
        }

        log.debug("User found: {}", user);

        return user.get();
    }

    /**
     * Updates an existing user.
     *
     * @param id      the id of the user to update
     * @param userDto the user data transfer object with the new values
     * @return the updated user data transfer object
     */
    @Override
    @Transactional
    public User updateUser(Long id, UserDto userDto) {
        log.info("Update user with id: {}", id);
        log.info("Update user with user: {}", userDto);
        User user = userMapper.toEntity(userDto);
        user.setId(id);
        String roleName = userDto.getRole().getName();
        Role role = roleService.getRoleByName(roleName);
        user.setRole(role);
        if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(userDto.getPassword());
            user.setPassword(encodedPassword);
        } else {
            User existingUser = userRepository.findById(id).orElseThrow(() ->
                    new EntityNotFoundException("User", id));
            user.setPassword(existingUser.getPassword());
        }
        User updatedUser = userRepository.save(user);
        log.debug("Updated user: {}", updatedUser);

        return user;
    }

    /**
     * Deletes a user.
     *
     * @param id the id of the user to delete
     */
    @Override
    public void deleteUser(Long id) {
        log.info("Delete user with id: {}", id);

        if (!userRepository.existsById(id)) {
            log.error("User not found with id: {}", id);
            throw new EntityNotFoundException("User", id);
        }

        userRepository.deleteById(id);
        log.debug("User deleted by id: {}", id);
    }

    /**
     * Lists users with a given filter and pagination.
     *
     * @param filter   the user filter data transfer object
     * @return a page of users
     */
    @Override
    public GroupResponseDto<UserDto> listUsers(UserFilterDto filter) {
        log.info("List users with filter: {}", filter);
        Specification<User> spec = createSpecification(filter);
        int size = (filter.getSize() != null) ? filter.getSize() : 10;
        Pageable pageable = PageRequest.of(filter.getPage(), size);
        Page<User> usersPage = userRepository.findAll(spec, pageable);
        List<UserDto> usersDto = usersPage.getContent().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
        log.debug("Found users: {}", usersDto);
        return new GroupResponseDto<>(usersDto, usersPage.getTotalPages());
    }

    /**
     * Generates a report for users matching a given filter.
     *
     * @param filter the user filter data transfer object
     * @return the generated report as a byte array
     */
    @Override
    public UserReportDto generateReport(UserFilterDto filter) {
        log.info("Generating report for filter: {}", filter);
        List<User> users = getAllUsersMatchingFilter(filter);

        UserReportType reportType = filter.getReportType();
        UserReportStrategy reportStrategy = reportStrategyFactory.lookup(reportType);

        byte[] content = reportStrategy.generateReport(users);
        String reportName = reportStrategy.generateReportName();

        return new UserReportDto(content, reportName);
    }

    public List<User> getAllUsersMatchingFilter(UserFilterDto filter) {
        log.info("Starting to get all users matching filter: {}", filter);
        Specification<User> spec = createSpecification(filter);
        List<User> users = userRepository.findAll(spec);
        log.debug("Found {} users matching filter", users.size());

        return users;
    }

    private Specification<User> createSpecification(UserFilterDto filter) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (filter.getRoleId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("role").get("id"), filter.getRoleId()));
            }
            if (filter.getUsername() != null) {
                predicates.add(criteriaBuilder.like(root.get("username"), "%" + filter.getUsername() + "%"));
            }
            if (filter.getEmail() != null) {
                predicates.add(criteriaBuilder.like(root.get("email"), "%" + filter.getEmail() + "%"));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    @Override
    public UserUploadResultDto uploadUsersFromFile(MultipartFile multipartFile) {
        log.info("Starting to upload users file");

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);

        try (InputStream inputStream = multipartFile.getInputStream();
             ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {

            Validator validator = factory.getValidator();

            List<UserDto> users = objectMapper.readValue(inputStream, new TypeReference<>() {
            });
            List<User> savedUsers = new ArrayList<>();

            users.parallelStream().forEach(user -> {
                Set<ConstraintViolation<UserDto>> violations = validator.validate(user);
                if (violations.isEmpty()) {
                    savedUsers.add(userMapper.toEntity(user));
                    successCount.incrementAndGet();
                } else {
                    failureCount.incrementAndGet();
                    log.warn("Validation failed for user: {}", user);
                }
            });
            userRepository.saveAll(savedUsers);

            log.info("Finished processing file. Success count: {}. Failure count: {}.",
                    successCount.get(), failureCount.get());
        } catch (Exception e) {
            log.error("Error processing upload file", e);
        }

        return new UserUploadResultDto(successCount.get(), failureCount.get());
    }

    @Override
    public List<UserDto> getAllUsers() {
        log.info("Getting all users");
        List<User> users = userRepository.findAll();
        List<UserDto> userDtos = users.stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
        log.debug("Found {} users", userDtos.size());
        return userDtos;
    }
}