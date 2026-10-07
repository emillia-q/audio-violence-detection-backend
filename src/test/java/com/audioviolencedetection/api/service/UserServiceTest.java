package com.audioviolencedetection.api.service;

import com.audioviolencedetection.api.dto.request.AddTrustedUserRequest;
import com.audioviolencedetection.api.entity.User;
import com.audioviolencedetection.api.entity.UserRelationship;
import com.audioviolencedetection.api.exception.BadRequestException;
import com.audioviolencedetection.api.repository.NotificationRepository;
import com.audioviolencedetection.api.repository.UserRelationshipRepository;
import com.audioviolencedetection.api.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    private static final Long PROTECTED_USER_ID = 1L;
    private static final Long TRUSTED_USER_ID = 2L;
    private static final String PROTECTED_USER_EMAIL = "protected@example.com";
    private static final String TRUSTED_USER_EMAIL = "trusted@example.com";

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserRelationshipRepository userRelationshipRepository;

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private UserService userService;

    @Captor
    private ArgumentCaptor<UserRelationship> relationshipCaptor;

    @Test
    void addTrustedUser_ShouldThrowBadRequestException_WhenAssigningSelf() {
        // GIVEN
        AddTrustedUserRequest request = new AddTrustedUserRequest(PROTECTED_USER_EMAIL, "My Guardian");

        User user = new User();
        user.setEmail(PROTECTED_USER_EMAIL);

        when(userRepository.findById(PROTECTED_USER_ID)).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(PROTECTED_USER_EMAIL)).thenReturn(Optional.of(user));

        // WHEN & THEN
        assertThrows(BadRequestException.class, () -> userService.addTrustedUser(request, PROTECTED_USER_ID));

        verify(userRelationshipRepository, never()).existsById(any());
        verify(userRelationshipRepository, never()).save(any());
    }
}
