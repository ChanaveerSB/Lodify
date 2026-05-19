package com.loadify.dto;

import com.loadify.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class SignupRequest {
    @NotBlank
    private String fullName;
    @Email
    @NotBlank
    private String email;
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must contain 10 digits")
    private String phone;
    @Size(min = 6, message = "Password must contain at least 6 characters")
    private String password;
    @NotBlank
    private String confirmPassword;
    @NotNull
    private UserRole role;

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getConfirmPassword() { return confirmPassword; }
    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }
    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }
}
