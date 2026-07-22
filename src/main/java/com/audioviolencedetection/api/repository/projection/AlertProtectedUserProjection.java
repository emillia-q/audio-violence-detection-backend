package com.audioviolencedetection.api.repository.projection;

import java.time.OffsetDateTime;

public interface AlertProtectedUserProjection {

    Long getAlertId();

    Long getProtectedUserId();

    String getProtectedUserNickname();

    String getDeviceName();

    OffsetDateTime getCreatedAt();
}
