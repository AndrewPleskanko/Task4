package com.example.block2.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private Long id;

    @NotBlank(message = "Username cannot be blank")
    @Size(min = 2, max = 30, message = "Username must be between 2 and 30 characters long")
    private String username;

    @Size(min = 5, max = 255, message = "Password must be between 6 and 50 characters long")
    private String password;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email cannot be blank")
    @Size(max = 50, message = "Email must be less than 50 characters long")
    private String email;

    @NotNull(message = "Role cannot be null")
    private RoleDto role;

    @Pattern(regexp = "^$|^[0-9]{10,15}$", message = "Phone number must be between 10 and 15 digits long")
    private String phone;

    @Max(value = 110, message = "Age should not be greater than 100")
    private Integer age;

}
