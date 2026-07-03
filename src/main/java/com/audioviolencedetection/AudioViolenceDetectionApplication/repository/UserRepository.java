package com.audioviolencedetection.AudioViolenceDetectionApplication.repository;

import com.audioviolencedetection.AudioViolenceDetectionApplication.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
