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

    @Query("select r.trustedUser.id as trustedUserId, r.nicknameForTrusted as trustedUserNickname " +
            "from UserRelationship r " +
            "where r.user.id = :userId")
    List<TrustedUserListProjection> findTrustedUsersByUserId(Long userId);

    @Query("select r.user.id as protectedUserId, r.nicknameForSupervised as protectedUserNickname " +
            "from UserRelationship r " +
            "where r.trustedUser.id = :userId")
    List<ProtectedUserListProjection> findProtectedUsersByUserId(Long userId);
}
