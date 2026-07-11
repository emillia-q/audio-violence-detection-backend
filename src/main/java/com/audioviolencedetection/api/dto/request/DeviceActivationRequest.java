package com.audioviolencedetection.api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record DeviceActivationRequest(
        @NotBlank(message = "MAC address is required")
        @Pattern(regexp = "^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$", message = "Invalid MAC address format")
        String macAddress,

        @NotBlank(message = "Device secret is required")
        String deviceSecret,

        @NotBlank(message = "User email is required")
        @Email(message = "Email should be valid")
        @Pattern(
                regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
                message = "Email must be a fully qualified address (e.g. user@example.com)"
        )
        String email
) {
}
