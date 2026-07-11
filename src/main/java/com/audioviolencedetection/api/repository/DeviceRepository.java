package com.audioviolencedetection.api.repository;

import com.audioviolencedetection.api.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {

    Optional<Device> findByMacAddress(String macAddress);
    List<Device> findAllByUserId(Long userId);
}
