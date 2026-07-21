package com.audioviolencedetection.api.mapper;

import com.audioviolencedetection.api.dto.response.AlertListResponse;
import com.audioviolencedetection.api.entity.Alert;
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
}
