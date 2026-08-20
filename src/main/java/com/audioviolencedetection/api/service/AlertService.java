package com.audioviolencedetection.api.service;

import com.audioviolencedetection.api.dto.response.AlertListResponse;
import com.audioviolencedetection.api.entity.*;
import com.audioviolencedetection.api.exception.ItemNotFoundException;
import com.audioviolencedetection.api.exception.UnprocessableEntityException;
import com.audioviolencedetection.api.mapper.AlertMapper;
import com.audioviolencedetection.api.repository.AlertRepository;
import com.audioviolencedetection.api.repository.DeviceRepository;
import com.audioviolencedetection.api.repository.NotificationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class AlertService {
    private final AlertRepository alertRepository;
    private final NotificationRepository notificationRepository;
    private final DeviceRepository deviceRepository;
    private final AlertMapper alertMapper;

    // Alerts from my devices
    public List<AlertListResponse> getListOfAlerts(Long userId, int pageNumber, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);

        return alertRepository.findAllByUserId(userId, pageRequest).stream()
                .map(alertMapper::toAlertListResponse)
                .toList();
    }

    @Transactional
    public void toggleNotificationStatusByAlertId(Long userId, Long alertId) {
        Notification notification = notificationRepository.findByAlertIdAndUserId(alertId, userId)
                .orElseThrow(() -> ItemNotFoundException.createForId(Notification.class, alertId));

        notification.setRead(!notification.isRead());
    }

    @Transactional
    public void deleteFalseAlert(Long userId, Long alertId) {
        // Check if alert belongs to a user - if it doesn't also throw exception
        Alert alert = alertRepository.findByIdAndDeviceUserId(alertId, userId)
                .orElseThrow(() -> ItemNotFoundException.createForId(Alert.class, alertId));

        alertRepository.delete(alert);
    }

    // Alerts sent by devices
    @Transactional
    public void sendAlertToDatabase(Long deviceId) {
        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> ItemNotFoundException.createForId(Device.class, deviceId));

        User protectedUser = device.getUser();
        if (protectedUser == null)
            throw new UnprocessableEntityException("Device must be activated and paired with a user before sending alerts");

        // Create & save alert
        Alert savedAlert = alertRepository.save(Alert.builder()
                .device(device)
                .build());

        // Collect all users to notify
        Stream<User> usersToNotify = Stream.concat(
                Stream.of(protectedUser),
                protectedUser.getTrustedRelations().stream()
                        .map(UserRelationship::getTrustedUser)
        );

        // Create & save notification for each trusted user
        List<Notification> notifications = usersToNotify
                .map(userToNotify -> Notification.builder()
                        .user(userToNotify)
                        .alert(savedAlert)
                        .build())
                .toList();

        // Save whole list at once
        notificationRepository.saveAll(notifications);
    }
}
