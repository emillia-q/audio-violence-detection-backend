package com.audioviolencedetection.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateDeviceNameRequest(
        @NotBlank(message = "Device name cannot be blank")
        @Size(max = 100, message = "Device name cannot exceed 100 characters")
        String name
) {
}
