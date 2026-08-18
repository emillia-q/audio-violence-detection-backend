package com.audioviolencedetection.api.service;

import com.audioviolencedetection.api.dto.response.NotificationListResponse;
import com.audioviolencedetection.api.entity.Notification;
import com.audioviolencedetection.api.exception.ItemNotFoundException;
import com.audioviolencedetection.api.mapper.NotificationMapper;
import com.audioviolencedetection.api.repository.NotificationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    public List<NotificationListResponse> getProtectedUsersNotifications(Long trustedUserId) {
        return notificationRepository.findProtectedUsersAlerts(trustedUserId).stream()
                .map(notificationMapper::toNotificationListResponse)
                .toList();
    }

    @Transactional
    public void toggleNotificationStatus(Long userId, Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> ItemNotFoundException.createForId(Notification.class, notificationId));

        // Check if the notification is connected to a logged user otherwise mask access
        if (!notification.getUser().getId().equals(userId))
            throw ItemNotFoundException.createForId(Notification.class, notificationId);

        notification.setRead(!notification.isRead());
    }
}
