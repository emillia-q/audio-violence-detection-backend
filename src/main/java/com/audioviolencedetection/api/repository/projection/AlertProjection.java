package com.audioviolencedetection.api.repository.projection;

import java.time.OffsetDateTime;

public interface AlertProjection {

    Long getId();

    String getDeviceName();

    OffsetDateTime getCreatedAt();
}
