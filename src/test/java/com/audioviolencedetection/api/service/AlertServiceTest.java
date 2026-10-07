package com.audioviolencedetection.api.service;

import com.audioviolencedetection.api.entity.*;
import com.audioviolencedetection.api.exception.ItemNotFoundException;
import com.audioviolencedetection.api.exception.UnprocessableEntityException;
import com.audioviolencedetection.api.repository.AlertRepository;
import com.audioviolencedetection.api.repository.DeviceRepository;
import com.audioviolencedetection.api.repository.NotificationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AlertServiceTest {

    private static final Long DEVICE_ID = 1L;

    @Mock
    private DeviceRepository deviceRepository;

    @Mock
    private AlertRepository alertRepository;

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private AlertService alertService;

    @Captor
    private ArgumentCaptor<List<Notification>> notificationsCaptor;

    @Test
    void sendAlertToDatabase_ShouldThrowItemNotFoundException_WhenDeviceDoesNotExist() {
        // GIVEN
        when(deviceRepository.findById(DEVICE_ID)).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(ItemNotFoundException.class, () -> alertService.sendAlertToDatabase(DEVICE_ID));

        verify(alertRepository, never()).save(any());
        verify(notificationRepository, never()).saveAll(any());
    }

    @ParameterizedTest(name = "isPaired, isActivated")
    @CsvSource({
            "false, false",
            "true, false",
            "false, true"
    })
    void sendAlertToDatabase_ShouldThrowUnprocessableEntityException_WhenDeviceNotReady(boolean isPaired, boolean isActivated) {
        // GIVEN
        Device device = new Device();
        device.setUser(isPaired ? new User() : null);
        device.setIsActivated(isActivated);

        when(deviceRepository.findById(DEVICE_ID)).thenReturn(Optional.of(device));

        // WHEN & THEN
        assertThrows(UnprocessableEntityException.class, () -> alertService.sendAlertToDatabase(DEVICE_ID),
                "Error expected: Device is not paired or not activated or both");

        verify(alertRepository, never()).save(any());
        verify(notificationRepository, never()).saveAll(any());
    }

    @Test
    void sendAlertToDatabase_ShouldSaveAlertAndSendNotificationsToAllRecipients() {
        // GIVEN
        // Create protected user & his trusted network
        User protectedUser = new User();
        protectedUser.setId(1L);
        User trustedUser1 = new User();
        trustedUser1.setId(2L);
        User trustedUser2 = new User();
        trustedUser2.setId(3L);

        UserRelationship userRelationship1 = new UserRelationship();
        userRelationship1.setTrustedUser(trustedUser1);
        UserRelationship userRelationship2 = new UserRelationship();
        userRelationship2.setTrustedUser(trustedUser2);
        Set<UserRelationship> relationships = new HashSet<>();
        relationships.add(userRelationship1);
        relationships.add(userRelationship2);
        protectedUser.setTrustedRelations(relationships);

        // Create device
        Device device = new Device();
        device.setUser(protectedUser);
        device.setIsActivated(true);

        Alert savedAlert = Alert.builder().device(device).build();

        when(deviceRepository.findById(DEVICE_ID)).thenReturn(Optional.of(device));
        when(alertRepository.save(any(Alert.class))).thenReturn(savedAlert);

        // WHEN
        alertService.sendAlertToDatabase(DEVICE_ID);

        // THEN
        // Check if alert is sent
        verify(alertRepository, times(1)).save(any(Alert.class));

        //  Capture saved notifications
        verify(notificationRepository, times(1)).saveAll(notificationsCaptor.capture());
        List<Notification> capturedNotifications = notificationsCaptor.getValue();

        // Verify size of the list and recipients
        assertNotNull(capturedNotifications);
        assertEquals(3, capturedNotifications.size(), "The list should contain 3 notifications");

        List<User> notifiedUsers = capturedNotifications.stream()
                .map(Notification::getUser)
                .toList();

        assertTrue(notifiedUsers.contains(protectedUser), "The notification should be received by protected user");
        assertTrue(notifiedUsers.contains(trustedUser1), "The notification should be received by trusted user 1");
        assertTrue(notifiedUsers.contains(trustedUser2), "The notification should be received by trusted user 2");
    }
}
