package com.audioviolencedetection.api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "First name is required")
        @Size(min = 2, max = 100, message = "First name must be between 2 and 100 characters")
        @Pattern(
                regexp = "^[\\p{L} \\-']+$",
                message = "First name can only contain letters, spaces, hyphens and apostrophes"
        )
        String firstName,

        @NotBlank(message = "Last name is required")
        @Size(min = 2, max = 100, message = "Last name must be between 2 and 100 characters")
        @Pattern(
                regexp = "^[\\p{L} \\-']+$",
                message = "Last name can only contain letters, spaces, hyphens and apostrophes"
        )
        String lastName,

        @NotBlank(message = "Email is required")
        @Email(message = "Email should be valid")
        @Pattern(
                regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
                message = "Email must be a fully qualified address (e.g. user@example.com)"
        )
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 8, max = 60, message = "Password must be at least 8 characters long")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$",
                message = "Password must contain at least one uppercase letter, one lowercase letter, one number and one special character"
        )
        String password
) {
    public RegisterRequest {
        if (firstName != null && !firstName.isBlank())
            firstName = formatName(firstName);

        if (lastName != null && !lastName.isBlank())
            lastName = formatName(lastName);

        if (email != null && !email.isBlank())
            email = email.trim().toLowerCase();
    }

    private static String formatName(String name) {
        String trimmed = name.trim();
        if (trimmed.length() == 1)
            return trimmed.toUpperCase(); // Guard clause
        return trimmed.substring(0,1).toUpperCase() + trimmed.substring(1).toLowerCase();
    }
}
