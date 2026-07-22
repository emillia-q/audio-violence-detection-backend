package com.audioviolencedetection.api.mapper;

import com.audioviolencedetection.api.dto.response.AlertListResponse;
import com.audioviolencedetection.api.dto.response.AlertProtectedUsersListResponse;
import com.audioviolencedetection.api.entity.Alert;
import com.audioviolencedetection.api.repository.projection.AlertProtectedUserProjection;
import org.springframework.stereotype.Component;

@Component
public class AlertMapper {

    public AlertListResponse toAlertListResponse(Alert alert) {
        return new AlertListResponse(
                alert.getId(),
                (alert.getDevice().getName() != null ? alert.getDevice().getName() : "Unnamed Device"),
                alert.getCreatedAt()
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
