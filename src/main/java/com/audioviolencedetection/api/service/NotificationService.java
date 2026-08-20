package com.audioviolencedetection.api.service;

import com.audioviolencedetection.api.dto.response.NotificationListResponse;
import com.audioviolencedetection.api.entity.Notification;
import com.audioviolencedetection.api.exception.ItemNotFoundException;
import com.audioviolencedetection.api.mapper.NotificationMapper;
import com.audioviolencedetection.api.repository.NotificationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    public List<NotificationListResponse> getProtectedUsersNotifications(Long trustedUserId, int pageNumber, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);

        return notificationRepository.findProtectedUsersAlerts(trustedUserId, pageRequest).stream()
                .map(notificationMapper::toNotificationListResponse)
                .toList();
    }

    @Transactional
    public void toggleNotificationStatus(Long userId, Long notificationId) {
        Notification notification = checkAccess(userId, notificationId);

        notification.setRead(!notification.isRead());
    }

    @Transactional
    public void deleteNotification(Long userId, Long notificationId) {
        Notification notification = checkAccess(userId, notificationId);

        notificationRepository.delete(notification);
    }

    private Notification checkAccess(Long userId, Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> ItemNotFoundException.createForId(Notification.class, notificationId));

        // Check if the notification is connected to a logged user otherwise mask access
        if (!notification.getUser().getId().equals(userId))
            throw ItemNotFoundException.createForId(Notification.class, notificationId);

        return notification;
    }
}
