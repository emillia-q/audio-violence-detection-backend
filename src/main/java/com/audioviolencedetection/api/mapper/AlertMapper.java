package com.audioviolencedetection.api.mapper;

import com.audioviolencedetection.api.dto.response.AlertListResponse;
import com.audioviolencedetection.api.dto.response.AlertProtectedUsersListResponse;
import com.audioviolencedetection.api.repository.projection.AlertProjection;
import com.audioviolencedetection.api.repository.projection.AlertProtectedUserProjection;
import org.springframework.stereotype.Component;

@Component
public class AlertMapper {

    public AlertListResponse toAlertListResponse(AlertProjection proj) {
        return new AlertListResponse(
                proj.getId(),
                (proj.getDeviceName() != null ? proj.getDeviceName() : "Unnamed Device"),
                proj.getCreatedAt()
        );
    }

    public AlertProtectedUsersListResponse toProtectedUsersAlertListResponse(AlertProtectedUserProjection proj) {
        return new AlertProtectedUsersListResponse(
                proj.getAlertId(),
                proj.getProtectedUserId(),
                proj.getProtectedUserNickname(),
                (proj.getDeviceName() != null ? proj.getDeviceName() : "Unnamed Device"),
                proj.getCreatedAt()
        );
    }
}
