package com.audioviolencedetection.api.dto.response;

public record TrustedUserDetailsResponse(
        Long id,
        String email,
        String customNickname
) {
}
