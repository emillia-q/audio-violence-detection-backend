package com.audioviolencedetection.api.mapper;

import com.audioviolencedetection.api.dto.response.TrustedUserListResponse;
import com.audioviolencedetection.api.repository.projection.TrustedUserListProjection;
import org.springframework.stereotype.Component;

@Component
public class UserRelationshipMapper {

    public TrustedUserListResponse toTrustedUserListResponse(TrustedUserListProjection proj) {
        return new TrustedUserListResponse(
                proj.getTrustedUserId(),
                proj.getTrustedUserNickname()
        );
    }
}
