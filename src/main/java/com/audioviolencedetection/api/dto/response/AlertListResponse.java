package com.audioviolencedetection.api.dto.response;

import java.time.OffsetDateTime;

public record AlertListResponse(
        Long id,
        String deviceName,
        OffsetDateTime createdAt
) {
}
