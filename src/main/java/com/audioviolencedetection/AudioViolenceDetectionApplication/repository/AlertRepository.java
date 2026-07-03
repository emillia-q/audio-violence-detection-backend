package com.audioviolencedetection.AudioViolenceDetectionApplication.repository;

import com.audioviolencedetection.AudioViolenceDetectionApplication.entity.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {
}
