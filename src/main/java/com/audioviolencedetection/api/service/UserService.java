package com.audioviolencedetection.api.service;

import com.audioviolencedetection.api.dto.request.AddTrustedUserRequest;
import com.audioviolencedetection.api.dto.response.TrustedUserDetailsResponse;
import com.audioviolencedetection.api.dto.response.TrustedUserListResponse;
import com.audioviolencedetection.api.entity.User;
import com.audioviolencedetection.api.entity.UserRelationship;
import com.audioviolencedetection.api.entity.UserRelationshipId;
import com.audioviolencedetection.api.exception.BadRequestException;
import com.audioviolencedetection.api.exception.ItemNotFoundException;
import com.audioviolencedetection.api.exception.ResourceInUseException;
import com.audioviolencedetection.api.repository.UserRelationshipRepository;
import com.audioviolencedetection.api.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserRelationshipRepository userRelationshipRepository;

    public List<TrustedUserListResponse> getListOfTrustedUsers(Long currentUserId) {
        return userRelationshipRepository.findTrustedUsersByUserId(currentUserId);
    }

    public TrustedUserDetailsResponse getTrustedUser(Long currentUserId, Long trustedUserId) {
        UserRelationshipId relationshipId = new UserRelationshipId(currentUserId, trustedUserId);
        UserRelationship relationship = userRelationshipRepository.findById(relationshipId)
                .orElseThrow(() -> ItemNotFoundException.createForId(UserRelationship.class, relationshipId));

        User trustedUser = relationship.getTrustedUser();
        return new TrustedUserDetailsResponse(
                trustedUser.getId(),
                trustedUser.getEmail(),
                relationship.getNicknameForTrusted()
        );
    }

    @Transactional
    public TrustedUserDetailsResponse addTrustedUser(AddTrustedUserRequest request, Long currentUserId) {
        // Check if users exist
        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> ItemNotFoundException.createForId(User.class, currentUserId));

        User trustedUser = userRepository.findByEmail(request.email())
                .orElseThrow(() -> ItemNotFoundException.createForEmail(User.class, request.email()));

        // Check if the user is not assigning himself
        if (request.email().equalsIgnoreCase(currentUser.getEmail()))
            throw new BadRequestException("You cannot set yourself as your own trusted user");

        // Create new relationship id & check if it does not already exist
        UserRelationshipId relationshipId = new UserRelationshipId(currentUserId, trustedUser.getId());
        if (userRelationshipRepository.existsById(relationshipId))
            throw new ResourceInUseException("This user is already assigned as your trusted user");

        // Create & save object
        UserRelationship relationship = new UserRelationship();
        relationship.setId(relationshipId);
        relationship.setUser(currentUser);
        relationship.setTrustedUser(trustedUser);
        relationship.setNicknameForTrusted(request.customNickname());

        return new TrustedUserDetailsResponse(
                trustedUser.getId(),
                trustedUser.getEmail(),
                relationship.getNicknameForTrusted()
                );
    }

    @Transactional
    public void deleteTrustedUser(Long currentUserId) {
        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> ItemNotFoundException.createForId(User.class, currentUserId));

        currentUser.setTrustedUser(null);
        userRepository.save(currentUser);
    }
}
