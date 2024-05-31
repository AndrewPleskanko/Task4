package com.example.block2.entity;

import java.io.Serial;
import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "app_user")
public class User extends AbstractEntity implements UserDetails {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "Username cannot be blank")
    @Size(min = 2, max = 50, message = "Username must be between 2 and 30 characters long")
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Size(min = 5, max = 255, message = "Password must be between 6 and 50 characters long")
    @Column(name = "password", nullable = false)
    private String password;

    @Email(message = "Email should be valid")
    @Size(max = 50, message = "Email must be less than 50 characters long")
    @Column(name = "email", nullable = false)
    private String email;

    @NotNull(message = "Role cannot be null")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Pattern(regexp = "^$|^[0-9]{10,15}$", message = "Phone number must be between 10 and 15 digits long")
    @Column(name = "phone")
    private String phone;

    @Max(value = 110, message = "Age should not be greater than 100")
    @Column(name = "age")
    private Integer age;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(role.getName()));

    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}