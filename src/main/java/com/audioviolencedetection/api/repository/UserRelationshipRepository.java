package com.audioviolencedetection.api.repository;

import com.audioviolencedetection.api.entity.UserRelationship;
import com.audioviolencedetection.api.entity.UserRelationshipId;
import com.audioviolencedetection.api.repository.projection.ProtectedUserListProjection;
import com.audioviolencedetection.api.repository.projection.TrustedUserListProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRelationshipRepository extends JpaRepository<UserRelationship, UserRelationshipId> {

    @Query("select r.trustedUser.id as trustedUserId, " +
            "case " +
            "when r.nicknameForTrusted = 'My Guardian' " +
            "then concat(r.trustedUser.firstName, ' ', r.trustedUser.lastName) " +
            "else r.nicknameForTrusted " +
            "end as trustedUserDisplayName " +
            "from UserRelationship r " +
            "where r.user.id = :userId")
    List<TrustedUserListProjection> findTrustedUsersByUserId(Long userId);

    @Query("select r.user.id as protectedUserId, " +
            "case " +
            "when r.nicknameForSupervised = 'My Supervised User' " +
            "then concat(r.user.firstName, ' ', r.user.lastName) " +
            "else r.nicknameForSupervised " +
            "end as protectedUserDisplayName " +
            "from UserRelationship r " +
            "where r.trustedUser.id = :userId")
    List<ProtectedUserListProjection> findProtectedUsersByUserId(Long userId);
}
