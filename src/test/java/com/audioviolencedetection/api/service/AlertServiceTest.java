package com.audioviolencedetection.api.service;

import com.audioviolencedetection.api.entity.Device;
import com.audioviolencedetection.api.entity.User;
import com.audioviolencedetection.api.exception.ItemNotFoundException;
import com.audioviolencedetection.api.exception.UnprocessableEntityException;
import com.audioviolencedetection.api.repository.AlertRepository;
import com.audioviolencedetection.api.repository.DeviceRepository;
import com.audioviolencedetection.api.repository.NotificationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

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
}
