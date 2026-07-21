package com.audioviolencedetection.api.service;

import com.audioviolencedetection.api.dto.response.AlertListResponse;
import com.audioviolencedetection.api.entity.Alert;
import com.audioviolencedetection.api.entity.Device;
import com.audioviolencedetection.api.entity.Notification;
import com.audioviolencedetection.api.entity.User;
import com.audioviolencedetection.api.exception.BadRequestException;
import com.audioviolencedetection.api.exception.ItemNotFoundException;
import com.audioviolencedetection.api.exception.UnprocessableEntityException;
import com.audioviolencedetection.api.mapper.AlertMapper;
import com.audioviolencedetection.api.repository.AlertRepository;
import com.audioviolencedetection.api.repository.DeviceRepository;
import com.audioviolencedetection.api.repository.NotificationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlertService {
    private final AlertRepository alertRepository;
    private final NotificationRepository notificationRepository;
    private final DeviceRepository deviceRepository;
    private final AlertMapper alertMapper;

    public List<AlertListResponse> getListOfAlerts(Long userId) {
        return alertRepository.findAllByUserId(userId).stream()
                .map(alertMapper::toAlertListResponse)
                .toList();
    }

    @Transactional
    public void sendAlertToDatabase(Long deviceId) {
        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> ItemNotFoundException.createForId(Device.class, deviceId));

        User protectedUser = device.getUser();
        if (protectedUser == null)
            throw new UnprocessableEntityException("Device must be activated and paired with a user before sending alerts");

        // Create & save alert
        Alert alert = Alert.builder()
                .device(device)
                .build();
        Alert savedAlert = alertRepository.save(alert);

        // Create & save notification for each trusted user
        List<Notification> notifications = protectedUser.getTrustedRelations().stream()
                .map(relation -> Notification.builder()
                        .trustedUser(relation.getTrustedUser())
                        .alert(savedAlert)
                        .build())
                .toList();

        // Save whole list at once
        if (!notifications.isEmpty())
            notificationRepository.saveAll(notifications);
    }
}
