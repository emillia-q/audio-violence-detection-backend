package com.audioviolencedetection.api.repository;

import com.audioviolencedetection.api.dto.response.TrustedUserListResponse;
import com.audioviolencedetection.api.entity.UserRelationship;
import com.audioviolencedetection.api.entity.UserRelationshipId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRelationshipRepository extends JpaRepository<UserRelationship, UserRelationshipId> {

    @Query("select new com.audioviolencedetection.api.dto.response.TrustedUserDetailsResponse(" +
            "r.trustedUser.id," +
            "r.trustedUser.nicknameForTrusted" +
            ")" +
            "from UserRelationship r" +
            "where r.userId = :userId")
    List<TrustedUserListResponse> findTrustedUsersByUserId(Long userId);
}
