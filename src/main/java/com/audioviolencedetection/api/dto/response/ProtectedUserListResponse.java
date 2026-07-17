package com.audioviolencedetection.api.dto.response;

public record ProtectedUserListResponse(
        Long protectedUserId,
        String protectedUserNickname
) {
}
