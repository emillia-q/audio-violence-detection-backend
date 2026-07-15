package com.audioviolencedetection.api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "email", length = 254, nullable = false, unique = true)
    private String email;

    @Column(name = "password", length = 60, nullable = false)
    private String password;

    @OneToMany(mappedBy = "trustedUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserRelationship> supervisedRelations = new HashSet<>();
}
