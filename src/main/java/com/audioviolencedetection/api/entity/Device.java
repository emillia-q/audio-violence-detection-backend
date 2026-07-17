package com.audioviolencedetection.api.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "devices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "mac_address", length = 17, nullable = false, unique = true)
    private String macAddress;

    @Column(name = "name", length = 100)
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(name = "is_activated", nullable = false)
    private Boolean isActivated = false;

    @Column(name = "device_secret", nullable = false, length = 64)
    private String deviceSecret;
}
