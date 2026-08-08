package com.audioviolencedetection.api.service;

import com.audioviolencedetection.api.dto.response.AlertListResponse;
import com.audioviolencedetection.api.dto.response.AlertProtectedUsersListResponse;
import com.audioviolencedetection.api.entity.*;
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
import java.util.stream.Stream;

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

    public List<AlertProtectedUsersListResponse> getListOfProtectedUsersAlerts(Long userId) {
        return alertRepository.findProtectedUsersAlerts(userId).stream()
                .map(alertMapper::toProtectedUsersAlertListResponse)
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
        if (!notifications.isEmpty())
            notificationRepository.saveAll(notifications);
    }
}
