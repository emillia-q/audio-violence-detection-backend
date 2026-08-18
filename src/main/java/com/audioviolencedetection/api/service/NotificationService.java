package com.audioviolencedetection.api.service;

import com.audioviolencedetection.api.dto.response.NotificationListResponse;
import com.audioviolencedetection.api.mapper.NotificationMapper;
import com.audioviolencedetection.api.repository.NotificationRepository;
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
}
