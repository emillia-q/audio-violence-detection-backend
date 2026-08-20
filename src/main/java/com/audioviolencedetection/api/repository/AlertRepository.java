package com.audioviolencedetection.api.repository;

import com.audioviolencedetection.api.entity.Alert;
import com.audioviolencedetection.api.repository.projection.AlertListProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {

    @Query("select a.id as id, a.device.name as deviceName, a.createdAt as createdAt, n.isRead as isRead " +
            "from Notification n " +
            "join n.alert a " +
            "where a.device.user.id = :userId " +
            "and n.user.id = :userId " +
            "order by a.createdAt desc")
    List<AlertListProjection> findAllByUserId(Long userId, Pageable pageable);

    Optional<Alert> findByIdAndDeviceUserId(Long alertId, Long userId);
}
