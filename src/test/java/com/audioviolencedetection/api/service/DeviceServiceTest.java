package com.audioviolencedetection.api.service;

import com.audioviolencedetection.api.dto.request.DeviceCredentialsRequest;
import com.audioviolencedetection.api.entity.Device;
import com.audioviolencedetection.api.entity.User;
import com.audioviolencedetection.api.exception.ResourceInUseException;
import com.audioviolencedetection.api.mapper.DeviceMapper;
import com.audioviolencedetection.api.repository.DeviceRepository;
import com.audioviolencedetection.api.repository.UserRepository;
import com.audioviolencedetection.api.util.CryptoUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeviceServiceTest {

    @Mock
    private DeviceRepository deviceRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private DeviceMapper deviceMapper;

    @InjectMocks
    private DeviceService deviceService;

    @Test
    void pairDevice_ShouldThrowResourceInUseException_WhenDeviceAlreadyHasUser() {
        // GIVEN
        Long userId = 1L;
        String macAddress = "AA:BB:CC:DD:EE:FF";
        String deviceSecret = "totalnietajnysekreturzadzenia123";
        DeviceCredentialsRequest request = new DeviceCredentialsRequest(macAddress, deviceSecret);
        Device device = new Device();
        device.setDeviceSecret(CryptoUtils.hashDeviceSecret(deviceSecret));
        device.setUser(new User()); // User assigned

        when(deviceRepository.findByMacAddress(macAddress)).thenReturn(Optional.of(device));

        // WHEN & THEN
        assertThrows(ResourceInUseException.class, () -> {
            deviceService.pairDevice(userId, request);
        }, "Expected ResourceInUseException, because the device was already paired with a user");

        verify(userRepository, never()).findById(anyLong());
    }
}
