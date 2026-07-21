package com.audioviolencedetection.api.repository;

import com.audioviolencedetection.api.entity.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {

    @Query("select a " +
            "from Alert a " +
            "where a.device.user.id = :userId " +
            "order by a.createdAt desc")
    List<Alert> findAllByUserId(Long userId);
}
