package com.audioviolencedetection.api.dto.response;

public record DeviceListResponse(
        Long id,
        String macAddress,
        String name
) {
}
