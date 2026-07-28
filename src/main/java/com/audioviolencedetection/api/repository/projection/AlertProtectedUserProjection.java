package com.audioviolencedetection.api.repository.projection;

import java.time.OffsetDateTime;

public interface AlertProtectedUserProjection {

    Long getAlertId();

    Long getProtectedUserId();

    String protectedUserDisplayName();

    String getDeviceName();

    OffsetDateTime getCreatedAt();
}
