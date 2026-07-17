package com.audioviolencedetection.api.dto.response;

public record ProtectedUserDetailsResponse(
        Long id,
        String email,
        String customNickname
) {
}
