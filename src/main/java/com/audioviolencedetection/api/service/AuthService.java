package com.audioviolencedetection.api.service;

import com.audioviolencedetection.api.dto.request.DeviceLoginRequest;
import com.audioviolencedetection.api.dto.request.LoginRequest;
import com.audioviolencedetection.api.dto.request.RegisterRequest;
import com.audioviolencedetection.api.dto.response.AuthResponse;
import com.audioviolencedetection.api.dto.response.DeviceLoginResponse;
import com.audioviolencedetection.api.entity.Device;
import com.audioviolencedetection.api.entity.User;
import com.audioviolencedetection.api.exception.InvalidDeviceSecretException;
import com.audioviolencedetection.api.exception.ItemNotFoundException;
import com.audioviolencedetection.api.exception.ResourceInUseException;
import com.audioviolencedetection.api.repository.DeviceRepository;
import com.audioviolencedetection.api.repository.UserRepository;
import com.audioviolencedetection.api.security.model.SecurityDevice;
import com.audioviolencedetection.api.security.model.SecurityUser;
import com.audioviolencedetection.api.security.service.JwtService;
import com.audioviolencedetection.api.util.CryptoUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final DeviceRepository deviceRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    // User
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        User user = userRepository.findByEmail(request.email()).orElseThrow();
        String token = generateTokenForUser(user);

        return new AuthResponse(token, user.getEmail());
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        // Check email availability
        String email = request.email();
        if (userRepository.existsByEmail(email))
            throw new ResourceInUseException("Email is already taken");

        User user = createAndSaveUser(request);

        String token = generateTokenForUser(user);

        return new AuthResponse(token, email);
    }

    private User createAndSaveUser(RegisterRequest request) {
        User user = User.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .build();
        return userRepository.save(user);
    }

    private String generateTokenForUser(User user) {
        SecurityUser securityUser = new SecurityUser(user);
        return jwtService.generateToken(securityUser, user.getId(), User.class.getSimpleName().toLowerCase());
    }

    // Device
    public DeviceLoginResponse authenticateDevice(DeviceLoginRequest request) {
        Device device = deviceRepository.findByMacAddress(request.macAddress())
                .orElseThrow(() -> ItemNotFoundException.createForMacAddress(Device.class, request.macAddress()));

        String incomingHash = CryptoUtils.hashDeviceSecret(request.deviceSecret());
        // Check if device secret is the same
        if (!incomingHash.equalsIgnoreCase(device.getDeviceSecret()))
            throw new InvalidDeviceSecretException("Invalid device secret");

        SecurityDevice securityDevice = new SecurityDevice(device);
        String token = jwtService.generateToken(securityDevice, device.getId(), Device.class.getSimpleName().toLowerCase());

        return new DeviceLoginResponse(token);
    }
}
