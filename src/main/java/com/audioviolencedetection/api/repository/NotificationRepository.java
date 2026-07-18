package com.audioviolencedetection.api.repository;

import com.audioviolencedetection.api.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
