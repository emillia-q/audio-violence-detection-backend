package com.audioviolencedetection.api.dto.response;

import java.time.OffsetDateTime;

public record AlertProtectedUsersListResponse(
        Long alertId,
        Long protectedUserId,
        String protectedUserDisplayName,
        String deviceName,
        OffsetDateTime createdAt
) {
}
