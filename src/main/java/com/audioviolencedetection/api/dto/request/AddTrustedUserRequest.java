package com.audioviolencedetection.api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AddTrustedUserRequest(
        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Invalid email format")
        String email,
        @Size(max = 100, message = "Nickname cannot be longer than 100 characters")
        @Pattern(
                regexp = "^[a-zA-Z0-9ąęćłńóśźżĄĘĆŁŃÓŚŹŻ ]*$",
                message = "Nickname can only contain letters, numbers and spaces"
        )
        String customNickname
) {
}
