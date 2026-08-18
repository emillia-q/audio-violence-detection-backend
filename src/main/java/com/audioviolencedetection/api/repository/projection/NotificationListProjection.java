package com.audioviolencedetection.api.repository.projection;

import java.time.OffsetDateTime;

public interface NotificationListProjection {

    Long getNotificationId();
    String getProtectedUserDisplayName();
    OffsetDateTime getCreatedAt();
    Boolean getIsRead();
}
