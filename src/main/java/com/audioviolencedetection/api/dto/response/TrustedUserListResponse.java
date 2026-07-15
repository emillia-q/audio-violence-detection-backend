package com.audioviolencedetection.api.dto.response;

public record TrustedUserListResponse(
        Long trustedUserId,
        String trustedUserNickname
) {
}
