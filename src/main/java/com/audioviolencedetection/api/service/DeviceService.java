package com.audioviolencedetection.api.service;

import com.audioviolencedetection.api.dto.response.DeviceDetailsResponse;
import com.audioviolencedetection.api.dto.response.DeviceListResponse;
import com.audioviolencedetection.api.entity.Device;
import com.audioviolencedetection.api.entity.User;
import com.audioviolencedetection.api.exception.ItemNotFoundException;
import com.audioviolencedetection.api.mapper.DeviceMapper;
import com.audioviolencedetection.api.repository.DeviceRepository;
import com.audioviolencedetection.api.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
    public DeviceDetailsResponse updateDeviceName(Long userId, Long deviceId, String name) {
        Device device = checkUserAccess(userId, deviceId);

        device.setName(name);
        return deviceMapper.toDeviceDetailsResponse(device);
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
