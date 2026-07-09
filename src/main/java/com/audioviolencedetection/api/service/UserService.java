package com.audioviolencedetection.api.service;

import com.audioviolencedetection.api.dto.request.AddTrustedUserRequest;
import com.audioviolencedetection.api.dto.response.TrustedUserDetailsResponse;
import com.audioviolencedetection.api.entity.User;
import com.audioviolencedetection.api.exception.BadRequestException;
import com.audioviolencedetection.api.exception.ItemNotFoundException;
import com.audioviolencedetection.api.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Optional<TrustedUserDetailsResponse> getTrustedUser(Long currentUserId) {
        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> ItemNotFoundException.createForId(User.class, currentUserId));

        // When trusted user id is null
        return Optional.ofNullable(currentUser.getTrustedUser())
                .map(trusted -> new TrustedUserDetailsResponse(trusted.getId(), trusted.getEmail()));
    }

    @Transactional
    public void setTrustedUser(AddTrustedUserRequest request, Long currentUserId) {
        // Check if users exist
        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> ItemNotFoundException.createForId(User.class, currentUserId));

        User trustedUser = userRepository.findByEmail(request.email())
                .orElseThrow(() -> ItemNotFoundException.createForEmail(User.class, request.email()));

        // Check if the user is not assigning himself
        if (request.email().equalsIgnoreCase(currentUser.getEmail()))
            throw new BadRequestException("You cannot set yourself as your own trusted user");

        currentUser.setTrustedUser(trustedUser);
        userRepository.save(currentUser);
    }

    @Transactional
    public void deleteTrustedUser(Long currentUserId) {
        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> ItemNotFoundException.createForId(User.class, currentUserId));

        currentUser.setTrustedUser(null);
        userRepository.save(currentUser);
    }
}
