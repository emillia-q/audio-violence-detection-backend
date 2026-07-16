package com.audioviolencedetection.api.service;

import com.audioviolencedetection.api.dto.request.AddTrustedUserRequest;
import com.audioviolencedetection.api.dto.request.ChangeNicknameRequest;
import com.audioviolencedetection.api.dto.response.ProtectedUserDetailsResponse;
import com.audioviolencedetection.api.dto.response.ProtectedUserListResponse;
import com.audioviolencedetection.api.dto.response.TrustedUserDetailsResponse;
import com.audioviolencedetection.api.dto.response.TrustedUserListResponse;
import com.audioviolencedetection.api.entity.User;
import com.audioviolencedetection.api.entity.UserRelationship;
import com.audioviolencedetection.api.entity.UserRelationshipId;
import com.audioviolencedetection.api.exception.BadRequestException;
import com.audioviolencedetection.api.exception.ItemNotFoundException;
import com.audioviolencedetection.api.exception.RelationshipNotFoundException;
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

    // Trusted Users
    public List<TrustedUserListResponse> getListOfTrustedUsers(Long protectedUserId) {
        return userRelationshipRepository.findTrustedUsersByUserId(protectedUserId);
    }

    public TrustedUserDetailsResponse getTrustedUser(Long protectedUserId, Long trustedUserId) {
        UserRelationshipId relationshipId = new UserRelationshipId(protectedUserId, trustedUserId);
        UserRelationship relationship = userRelationshipRepository.findById(relationshipId)
                .orElseThrow(() -> new RelationshipNotFoundException("Trusted user relationship not found"));

        return new TrustedUserDetailsResponse(
                relationship.getTrustedUser().getId(),
                relationship.getTrustedUser().getEmail(),
                relationship.getNicknameForTrusted()
        );
    }

    @Transactional
    public TrustedUserDetailsResponse addTrustedUser(AddTrustedUserRequest request, Long protectedUserId) {
        // Check if users exist
        User currentUser = userRepository.findById(protectedUserId)
                .orElseThrow(() -> ItemNotFoundException.createForId(User.class, protectedUserId));

        User trustedUser = userRepository.findByEmail(request.email())
                .orElseThrow(() -> ItemNotFoundException.createForEmail(User.class, request.email()));

        // Check if the user is not assigning himself
        if (request.email().equalsIgnoreCase(currentUser.getEmail()))
            throw new BadRequestException("You cannot set yourself as your own trusted user");

        // Create new relationship id & check if it does not already exist
        UserRelationshipId relationshipId = new UserRelationshipId(protectedUserId, trustedUser.getId());
        if (userRelationshipRepository.existsById(relationshipId))
            throw new ResourceInUseException("This user is already assigned as your trusted user");

        // Create & save object
        UserRelationship relationship = new UserRelationship();
        relationship.setId(relationshipId);
        relationship.setUser(currentUser);
        relationship.setTrustedUser(trustedUser);

        // If user sent custom nick -> save it
        // If he didn't -> entity uses its  domain value
        if (request.customNickname() != null && !request.customNickname().isBlank())
            relationship.setNicknameForTrusted(request.customNickname());

        userRelationshipRepository.save(relationship);
        return new TrustedUserDetailsResponse(
                trustedUser.getId(),
                trustedUser.getEmail(),
                relationship.getNicknameForTrusted()
        );
    }

    @Transactional
    public TrustedUserDetailsResponse changeTrustedUserNickname(Long protectedUserId, Long trustedUserId, ChangeNicknameRequest request) {
        UserRelationshipId relationshipId = new UserRelationshipId(protectedUserId, trustedUserId);
        UserRelationship relationship = userRelationshipRepository.findById(relationshipId)
                .orElseThrow(() -> new RelationshipNotFoundException("Trusted user relationship not found"));

        if (request.customNickname() == null)
            relationship.setNicknameForTrusted("My Guardian");
        else
            relationship.setNicknameForTrusted(request.customNickname());
        return new TrustedUserDetailsResponse(
                relationship.getTrustedUser().getId(),
                relationship.getTrustedUser().getEmail(),
                relationship.getNicknameForTrusted()
        );
    }

    // Protected Users
    public List<ProtectedUserListResponse> getListOfProtectedUsers(Long trustedUserId) {
        return userRelationshipRepository.findProtectedUsersByUserId(trustedUserId);
    }

    public ProtectedUserDetailsResponse getProtectedUser(Long trustedUserId, Long protectedUserId) {
        UserRelationshipId relationshipId = new UserRelationshipId(protectedUserId, trustedUserId);
        UserRelationship relationship = userRelationshipRepository.findById(relationshipId)
                .orElseThrow(() -> new RelationshipNotFoundException("Protected user relationship not found"));

        return new ProtectedUserDetailsResponse(
                relationship.getUser().getId(),
                relationship.getUser().getEmail(),
                relationship.getNicknameForSupervised()
        );
    }

    @Transactional
    public ProtectedUserDetailsResponse changeProtectedUserNickname(Long trustedUserId, Long protectedUserId, ChangeNicknameRequest request) {
        UserRelationshipId relationshipId = new UserRelationshipId(protectedUserId, trustedUserId);
        UserRelationship relationship = userRelationshipRepository.findById(relationshipId)
                .orElseThrow(() -> new RelationshipNotFoundException("Protected user relationship not found"));

        if (request.customNickname() == null)
            relationship.setNicknameForSupervised("My Supervised User");
        else
            relationship.setNicknameForSupervised(request.customNickname());
        return new ProtectedUserDetailsResponse(
                relationship.getUser().getId(),
                relationship.getUser().getEmail(),
                relationship.getNicknameForSupervised()
        );
    }

    // Shared
    @Transactional
    public void deleteRelatedUser(Long protectedUserId, Long trustedUserId) {
        UserRelationshipId relationshipId = new UserRelationshipId(protectedUserId, trustedUserId);
        if (!userRelationshipRepository.existsById(relationshipId))
            throw new RelationshipNotFoundException("Trusted user relationship not found");

        userRelationshipRepository.deleteById(relationshipId);
    }
}
