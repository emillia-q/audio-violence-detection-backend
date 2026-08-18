package com.audioviolencedetection.api.dto.response;

import java.time.OffsetDateTime;

public record NotificationListResponse(
        Long notificationId,
        String protectedUserDisplayName,
        OffsetDateTime createdAt,
        Boolean isRead
) {
}
