package com.audioviolencedetection.api.repository.projection;

import java.time.OffsetDateTime;

public interface AlertListProjection {

    Long getId();

    String getDeviceName();

    OffsetDateTime getCreatedAt();

    Boolean getIsRead();
}
