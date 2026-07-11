package com.audioviolencedetection.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateDeviceNameRequest(
        @NotBlank
        @Size(max = 100)
        String name
) {
}
