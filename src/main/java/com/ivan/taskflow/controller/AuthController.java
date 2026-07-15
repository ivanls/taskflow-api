package com.ivan.taskflow.controller;

import com.ivan.taskflow.dto.CreateTaskRequest;
import com.ivan.taskflow.dto.RegisterRequest;
import com.ivan.taskflow.dto.RegisterResponse;
import com.ivan.taskflow.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public RegisterResponse registerUser(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }
}
