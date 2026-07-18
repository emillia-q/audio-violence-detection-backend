package com.audioviolencedetection.api.service;

import com.audioviolencedetection.api.entity.Alert;
import com.audioviolencedetection.api.entity.Device;
import com.audioviolencedetection.api.exception.ItemNotFoundException;
import com.audioviolencedetection.api.repository.AlertRepository;
import com.audioviolencedetection.api.repository.DeviceRepository;
import com.audioviolencedetection.api.repository.NotificationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlertService {
    private final AlertRepository alertRepository;
    private final NotificationRepository notificationRepository;
    private final DeviceRepository deviceRepository;

    @Transactional
    public void sendAlertToDatabase(Long deviceId) {
        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> ItemNotFoundException.createForId(Device.class, deviceId));

        // Create & save alert
        Alert alert = Alert.builder()
                .device(device)
                .build();
        alertRepository.save(alert);


    }
}
