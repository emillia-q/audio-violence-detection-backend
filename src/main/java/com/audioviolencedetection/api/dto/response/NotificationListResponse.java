package com.audioviolencedetection.api.dto.response;

import java.time.OffsetDateTime;

public record NotificationListResponse(
        Long notificationId,
        String protectedUserName,
        OffsetDateTime createdAt,
        Boolean isRead
) {
}
