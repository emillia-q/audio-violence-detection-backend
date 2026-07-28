package com.audioviolencedetection.api.dto.response;

public record TrustedUserDetailsResponse(
        Long id,
        String firstName,
        String lastName,
        String customNickname
) {
}
