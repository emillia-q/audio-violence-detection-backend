package com.audioviolencedetection.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRelationshipId implements Serializable {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "trusted_user_id")
    private Long trustedUserId;
}
