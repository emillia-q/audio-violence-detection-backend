package com.audioviolencedetection.api.service;

import com.audioviolencedetection.api.dto.request.DeviceActivationRequest;
import com.audioviolencedetection.api.dto.request.UpdateDeviceNameRequest;
import com.audioviolencedetection.api.dto.response.DeviceDetailsResponse;
import com.audioviolencedetection.api.dto.response.DeviceListResponse;
import com.audioviolencedetection.api.entity.Device;
import com.audioviolencedetection.api.entity.User;
import com.audioviolencedetection.api.exception.CryptoException;
import com.audioviolencedetection.api.exception.InvalidDeviceSecretException;
import com.audioviolencedetection.api.exception.ItemNotFoundException;
import com.audioviolencedetection.api.exception.ResourceInUseException;
import com.audioviolencedetection.api.mapper.DeviceMapper;
import com.audioviolencedetection.api.repository.DeviceRepository;
import com.audioviolencedetection.api.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.buf.HexUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DeviceService {

    private final DeviceRepository deviceRepository;
    private final UserRepository userRepository;
    private final DeviceMapper deviceMapper;

    public List<DeviceListResponse> getUserDevices(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> ItemNotFoundException.createForId(User.class, userId));

        return deviceRepository.findAllByUserId(userId)
                .stream()
                .map(deviceMapper::toDeviceListResponse)
                .toList();
    }

    public DeviceDetailsResponse getDeviceDetails(Long userId, Long deviceId) {
        Device device = checkUserAccess(userId, deviceId);

        return deviceMapper.toDeviceDetailsResponse(device);
    }

    @Transactional
    public void activateAndPairDevice(DeviceActivationRequest request) {
        Device device = deviceRepository.findByMacAddress(request.macAddress())
                .orElseThrow(() -> ItemNotFoundException.createForMacAddress(Device.class, request.macAddress()));

        String incomingHash = hashDeviceSecret(request.deviceSecret());
        // Check if device secret is the same
        if (!incomingHash.equalsIgnoreCase(device.getDeviceSecret()))
            throw new InvalidDeviceSecretException("Invalid device secret");

        // Check if device already has a user
        if (device.getUser() != null)
            throw new ResourceInUseException("This device is already signed to a user");

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> ItemNotFoundException.createForEmail(User.class, request.email()));

        // Assign a new user to the device & activate
        device.setUser(user);
        device.setIsActivated(true);
    }

    private String hashDeviceSecret(String secret) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(secret.getBytes(StandardCharsets.UTF_8));
            return HexUtils.toHexString(encodedHash);
        } catch (NoSuchAlgorithmException e) {
            throw new CryptoException("SHA-256 algorithm not available", e);
        }
    }

    @Transactional
    public DeviceDetailsResponse updateDeviceName(Long userId, Long deviceId, UpdateDeviceNameRequest request) {
        Device device = checkUserAccess(userId, deviceId);

        device.setName(request.name());
        return deviceMapper.toDeviceDetailsResponse(device);
    }

    @Transactional
    public void disconnectDevice(Long userId, Long deviceId) {
        Device device = checkUserAccess(userId, deviceId);

        device.setIsActivated(false);
        device.setName(null);
        device.setUser(null);
    }

    private Device checkUserAccess(Long userId, Long deviceId) {
        userRepository.findById(userId)
                .orElseThrow(() -> ItemNotFoundException.createForId(User.class, userId));

        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> ItemNotFoundException.createForId(Device.class, deviceId));

        // Mask access denied
        if (device.getUser() == null || !userId.equals(device.getUser().getId()))
            throw ItemNotFoundException.createForId(Device.class, deviceId);

        return device;
    }
}
