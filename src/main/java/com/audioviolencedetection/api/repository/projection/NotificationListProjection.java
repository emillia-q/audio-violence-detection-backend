package com.audioviolencedetection.api.repository.projection;

import java.time.OffsetDateTime;

public interface NotificationListProjection {

    Long getId();
    String getProtectedUserName();
    OffsetDateTime getCreatedAt();
    Boolean getIsRead();
}
