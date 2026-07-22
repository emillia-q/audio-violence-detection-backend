package com.audioviolencedetection.api.repository;

import com.audioviolencedetection.api.entity.Alert;
import com.audioviolencedetection.api.repository.projection.AlertProjection;
import com.audioviolencedetection.api.repository.projection.AlertProtectedUserProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {

    @Query("select a.id as id, a.device.name as deviceName, a.createdAt as createdAt " +
            "from Alert a " +
            "where a.device.user.id = :userId " +
            "order by a.createdAt desc")
    List<AlertProjection> findAllByUserId(Long userId);

    @Query("select a.id as alertId, r.user.id as protectedUserId, r.nicknameForSupervised as protectedUserNickname, " +
            "d.name as deviceName, a.createdAt as createdAt " +
            "from Alert a " +
            "join a.device d " +
            "join UserRelationship r on r.user.id = d.user.id " +
            "where r.trustedUser.id = :userId " +
            "order by a.createdAt desc")
    List<AlertProtectedUserProjection> findProtectedUsersAlerts(Long userId);
}
