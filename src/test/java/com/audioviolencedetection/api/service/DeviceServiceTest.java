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

    private static final Long USER_ID = 1L;
    private static final String MAC_ADDRESS = "AA:BB:CC:DD:EE:FF";
    private static final String DEVICE_SECRET = "totalnietajnysekreturzadzenia123";

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
        DeviceCredentialsRequest request = new DeviceCredentialsRequest(MAC_ADDRESS, DEVICE_SECRET);

        Device device = createValidDevice();
        device.setUser(new User()); // User assigned

        when(deviceRepository.findByMacAddress(MAC_ADDRESS)).thenReturn(Optional.of(device));

        // WHEN & THEN
        assertThrows(ResourceInUseException.class, () -> {
            deviceService.pairDevice(USER_ID, request);
        }, "Expected ResourceInUseException, because the device was already paired with a user");

        verify(userRepository, never()).findById(anyLong());
    }

    private Device createValidDevice() {
        Device device = new Device();
        device.setDeviceSecret(CryptoUtils.hashDeviceSecret(DEVICE_SECRET));
        return device;
    }
}
