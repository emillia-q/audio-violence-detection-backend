package com.audioviolencedetection.api.service;

import com.audioviolencedetection.api.dto.response.DeviceDetailsResponse;
import com.audioviolencedetection.api.dto.response.DeviceListResponse;
import com.audioviolencedetection.api.entity.Device;
import com.audioviolencedetection.api.entity.User;
import com.audioviolencedetection.api.exception.ItemNotFoundException;
import com.audioviolencedetection.api.mapper.DeviceMapper;
import com.audioviolencedetection.api.repository.DeviceRepository;
import com.audioviolencedetection.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        userRepository.findById(userId)
                .orElseThrow(() -> ItemNotFoundException.createForId(User.class, userId));

        return deviceRepository.findById(deviceId)
                .map(deviceMapper::toDeviceDetailsResponse)
                .orElseThrow(() -> ItemNotFoundException.createForId(Device.class, deviceId));
    }
}
