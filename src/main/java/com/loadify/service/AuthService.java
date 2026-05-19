package com.loadify.service;

import com.loadify.dao.UserDao;
import com.loadify.dto.LoginRequest;
import com.loadify.dto.SignupRequest;
import com.loadify.dto.UserResponse;
import com.loadify.entity.User;
import com.loadify.exception.DuplicateEmailException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserDao userDao;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthService(UserDao userDao, BCryptPasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse signup(SignupRequest request) {
        if (userDao.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException("Email already registered");
        }
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Password and confirm password do not match");
        }
        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setRole(request.getRole());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        return toResponse(userDao.save(user));
    }

    public UserResponse login(LoginRequest request) {
        User user = userDao.findByEmail(request.getEmail());
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }
        return toResponse(user);
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(user.getUserId(), user.getFullName(), user.getEmail(), user.getPhone(), user.getRole());
    }
}
