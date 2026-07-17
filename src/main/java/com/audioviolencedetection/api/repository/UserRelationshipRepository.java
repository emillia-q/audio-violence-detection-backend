package com.audioviolencedetection.api.repository;

import com.audioviolencedetection.api.dto.response.ProtectedUserListResponse;
import com.audioviolencedetection.api.dto.response.TrustedUserListResponse;
import com.audioviolencedetection.api.entity.UserRelationship;
import com.audioviolencedetection.api.entity.UserRelationshipId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRelationshipRepository extends JpaRepository<UserRelationship, UserRelationshipId> {

    @Query("select new com.audioviolencedetection.api.dto.response.TrustedUserListResponse(" +
            "r.trustedUser.id, " +
            "r.nicknameForTrusted" +
            ") " +
            "from UserRelationship r " +
            "where r.user.id = :userId")
    List<TrustedUserListResponse> findTrustedUsersByUserId(Long userId);

    @Query("select new com.audioviolencedetection.api.dto.response.ProtectedUserListResponse(" +
            "r.user.id, " +
            "r.nicknameForSupervised" +
            ") " +
            "from UserRelationship r " +
            "where r.trustedUser.id = :userId")
    List<ProtectedUserListResponse> findProtectedUsersByUserId(Long userId);
}
