package com.loadify.service;

import com.loadify.entity.User;
import com.loadify.enums.UserRole;
import com.loadify.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder; // MUST IMPORT THIS
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // This makes the red line on .encode() go away

    public User registerUser(User user) {
        // SECURITY GUARD: Compare Enum to Enum
        // Use == for Enums; it is null-safe and very fast
        if (user.getRole() == UserRole.ADMIN) {
            user.setRole(UserRole.CUSTOMER);
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
}