package com.loadify.controller;

import com.loadify.dto.LoginRequest;
import com.loadify.dto.SignupRequest;
import com.loadify.dto.UserResponse;
import com.loadify.service.AuthService;
import com.loadify.util.ResponseStructure;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<ResponseStructure<UserResponse>> signup(@Valid @RequestBody SignupRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseStructure.success(201, "Signup successful", authService.signup(request)));
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseStructure<UserResponse>> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(ResponseStructure.success(200, "Login successful", authService.login(request)));
    }
}
