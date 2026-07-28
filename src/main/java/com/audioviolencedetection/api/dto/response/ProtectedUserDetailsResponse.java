package com.audioviolencedetection.api.dto.response;

public record ProtectedUserDetailsResponse(
        Long id,
        String firstName,
        String lastName,
        String customNickname
) {
}
