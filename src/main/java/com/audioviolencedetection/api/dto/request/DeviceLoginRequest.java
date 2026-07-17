package com.audioviolencedetection.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record DeviceLoginRequest(
        @NotBlank(message = "MAC address is required")
        @Pattern(regexp = "^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$", message = "Invalid MAC address format")
        String macAddress,

        @NotBlank(message = "Device secret is required")
        String deviceSecret
) {
}
