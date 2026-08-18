package com.audioviolencedetection.api.repository.projection;

import java.time.OffsetDateTime;

public interface NotificationListProjection {

    Long getNotificationId();
    String getProtectedUserName();
    OffsetDateTime getCreatedAt();
    Boolean getIsRead();
}
