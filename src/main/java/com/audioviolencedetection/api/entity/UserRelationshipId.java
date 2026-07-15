package com.audioviolencedetection.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserRelationshipId that = (UserRelationshipId) o;
        return Objects.equals(userId, that.userId) && Objects.equals(trustedUserId, that.trustedUserId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, trustedUserId);
    }
}
