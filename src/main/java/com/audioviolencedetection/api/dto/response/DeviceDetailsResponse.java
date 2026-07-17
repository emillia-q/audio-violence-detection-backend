package com.audioviolencedetection.api.dto.response;

public record DeviceDetailsResponse(
        Long id,
        String macAddress,
        String name,
        Boolean isActivated
) {
}
