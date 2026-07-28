package com.audioviolencedetection.api.dto.response;

public record AuthResponse(
        String token,
        String email,
        String firstName,
        String lastName
) {
}
