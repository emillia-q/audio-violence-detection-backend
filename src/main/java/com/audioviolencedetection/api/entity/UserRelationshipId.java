package com.audioviolencedetection.api.entity;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class UserRelationshipId implements Serializable {
    private Long userId;
    private Long trustedUserId;
}
