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

    @EmbeddedId
    private UserRelationshipId id;

    @ManyToOne
    @MapsId("userId") // Take pk from embeddable class & use it as fk in users
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("trustedUserId")
    @JoinColumn(name = "trusted_user_id")
    private User trustedUser;

    // Set by the supervised user (victim) for their guardian (trusted user)
    @Column(name = "nickname_for_trusted", length = 100)
    private String nicknameForTrusted = "My Guardian";

    // Set by the guardian (trusted user) for their supervised user (victim)
    @Column(name = "nickname_for_supervised", length = 100)
    private String nicknameForSupervised = "My Supervised User";
}
