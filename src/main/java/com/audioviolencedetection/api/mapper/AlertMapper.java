package com.audioviolencedetection.api.mapper;

import com.audioviolencedetection.api.dto.response.AlertListResponse;
import com.audioviolencedetection.api.repository.projection.AlertListProjection;
import org.springframework.stereotype.Component;

@Component
public class AlertMapper {

    public AlertListResponse toAlertListResponse(AlertListProjection proj) {
        return new AlertListResponse(
                proj.getId(),
                (proj.getDeviceName() != null ? proj.getDeviceName() : "Unnamed Device"),
                proj.getCreatedAt(),
                proj.getIsRead()
        );
    }
}
