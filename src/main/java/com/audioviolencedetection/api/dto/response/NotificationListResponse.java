package com.audioviolencedetection.api.dto.response;

import java.time.OffsetDateTime;

public record NotificationListResponse(
        Long id,
        String protectedUserName,
        OffsetDateTime createdAt,
        Boolean isRead
) {
}
