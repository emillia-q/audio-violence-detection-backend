package com.audioviolencedetection.api.mapper;

import com.audioviolencedetection.api.dto.response.NotificationListResponse;
import com.audioviolencedetection.api.repository.projection.NotificationListProjection;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {

    public NotificationListResponse toNotificationListResponse(NotificationListProjection proj) {
        return new NotificationListResponse(
                proj.getNotificationId(),
                proj.getProtectedUserDisplayName(),
                proj.getCreatedAt(),
                proj.getIsRead()
        );
    }
}
