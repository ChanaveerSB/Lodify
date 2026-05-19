package com.loadify.dto;

import com.loadify.enums.UserRole;

public class UserResponse {
    private Long userId;
    private String fullName;
    private String email;
    private String phone;
    private UserRole role;

    public UserResponse(Long userId, String fullName, String email, String phone, UserRole role) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.role = role;
    }

    public Long getUserId() { return userId; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public UserRole getRole() { return role; }
}
