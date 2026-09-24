package com.audioviolencedetection.api.security.service;

import com.audioviolencedetection.api.repository.DeviceRepository;
import com.audioviolencedetection.api.security.model.SecurityDevice;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomDeviceDetailsService {
    private final DeviceRepository deviceRepository;

    public UserDetails loadDeviceById(Long deviceId) {
        return deviceRepository.findById(deviceId)
                .map(SecurityDevice::new)
                .orElse(null);
    }
}
