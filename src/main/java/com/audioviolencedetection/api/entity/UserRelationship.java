package com.audioviolencedetection.api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_relationships")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRelationship {

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("trustedUserId")
    @JoinColumn(name = "trusted_user_id")
    private User trustedUser;

    @Column(name = "custom_nickname")
    private String customNickname;
}
