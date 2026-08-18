package com.audioviolencedetection.api.repository;

import com.audioviolencedetection.api.entity.Notification;
import com.audioviolencedetection.api.repository.projection.NotificationListProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Modifying
    @Query("delete from Notification n " +
            "where n.alert.device.user.id = :protectedUserId " +
            "and n.user.id = :trustedUserId")
    void deleteByTrustedUserAndDeviceOwner(Long protectedUserId, Long trustedUserId);

    @Query("select n.id as notificationId, " +
            "case " +
            "when r.nicknameForSupervised = 'My Supervised User' " +
            "then concat(r.user.firstName, ' ', r.user.lastName) " +
            "else r.nicknameForSupervised " +
            "end as protectedUserDisplayName, " +
            "n.createdAt as createdAt, " +
            "n.isRead as isRead " +
            "from Notification n " +
            "join UserRelationship r on r.trustedUser.id = n.user.id " +
            "and r.user.id = n.alert.device.user.id " +
            "where r.trustedUser.id = :trustedUserId " +
            "order by n.createdAt desc")
    List<NotificationListProjection> findProtectedUsersAlerts(Long trustedUserId);
}
