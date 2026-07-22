package com.audioviolencedetection.api.dto.response;

import java.time.OffsetDateTime;

public record AlertProtectedUsersListResponse(
        Long id,
        String protectedUserNickname,
        String deviceName,
        OffsetDateTime createdAt
) {
}
