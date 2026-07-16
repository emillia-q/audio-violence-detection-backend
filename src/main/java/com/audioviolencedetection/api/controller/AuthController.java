package com.audioviolencedetection.api.controller;

import com.audioviolencedetection.api.dto.request.DeviceLoginRequest;
import com.audioviolencedetection.api.dto.request.LoginRequest;
import com.audioviolencedetection.api.dto.request.RegisterRequest;
import com.audioviolencedetection.api.dto.response.AuthResponse;
import com.audioviolencedetection.api.dto.response.DeviceLoginResponse;
import com.audioviolencedetection.api.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // User
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Authenticate user and return JWT token")
    @ApiResponse(responseCode = "200", description = "User successfully logged in")
    @ApiResponse(responseCode = "400", description = "Invalid request data or validation failed")
    @ApiResponse(responseCode = "401", description = "Invalid email or password")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registers a new user account in the system")
    @ApiResponse(responseCode = "201", description = "User successfully registered")
    @ApiResponse(responseCode = "400", description = "Invalid request data or validation failed")
    @ApiResponse(responseCode = "409", description = "Email address is already taken")
    public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    // Device
    @PostMapping("/device-login")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Authenticate device and return JWT token")
    @ApiResponse(responseCode = "200", description = "Device successfully authenticated")
    @ApiResponse(responseCode = "400", description = "Invalid request data or validation failed")
    @ApiResponse(responseCode = "401", description = "Invalid MAC address or device secret key")
    public DeviceLoginResponse authenticateDevice(@Valid @RequestBody DeviceLoginRequest request) {

    }
}
