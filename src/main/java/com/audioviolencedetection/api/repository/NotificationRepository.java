package com.audioviolencedetection.api.repository;

import com.audioviolencedetection.api.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Modifying
    @Query("delete from Notification n " +
            "where n.trustedUser.id = :trustedUserId " +
            "and n.alert.device.user.id = :protectedUserId")
    void deleteByTrustedUserAndDeviceOwner(Long protectedUserId, Long trustedUserId);
}
