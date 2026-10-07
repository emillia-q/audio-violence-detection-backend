package com.audioviolencedetection.api.service;

import com.audioviolencedetection.api.dto.request.DeviceCredentialsRequest;
import com.audioviolencedetection.api.dto.response.DeviceDetailsResponse;
import com.audioviolencedetection.api.entity.Device;
import com.audioviolencedetection.api.entity.User;
import com.audioviolencedetection.api.exception.InvalidDeviceCredentialsException;
import com.audioviolencedetection.api.exception.ItemNotFoundException;
import com.audioviolencedetection.api.exception.ResourceInUseException;
import com.audioviolencedetection.api.mapper.DeviceMapper;
import com.audioviolencedetection.api.repository.DeviceRepository;
import com.audioviolencedetection.api.repository.UserRepository;
import com.audioviolencedetection.api.util.CryptoUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeviceServiceTest {

    private static final Long USER_ID = 1L;
    private static final Long DEVICE_ID = 1L;
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
    void pairDevice_ShouldThrowInvalidDeviceCredentialsException_WhenDeviceIsNotInDatabase() {
        // GIVEN
        DeviceCredentialsRequest request = new DeviceCredentialsRequest(MAC_ADDRESS, DEVICE_SECRET);

        // No device with this mac address in db
        when(deviceRepository.findByMacAddress(MAC_ADDRESS)).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(InvalidDeviceCredentialsException.class, () -> deviceService.pairDevice(USER_ID, request));

        verify(userRepository, never()).findById(anyLong());
        verify(deviceMapper, never()).toDeviceDetailsResponse(any());
    }

    @ParameterizedTest
    @ValueSource(strings = {"zlysekret", "2093648", "ak7wo3me1rk2", ""})
    void pairDevice_ShouldThrowInvalidDeviceCredentialsException_WhenSecretIsWrong(String wrongSecret) {
        // GIVEN
        DeviceCredentialsRequest request = new DeviceCredentialsRequest(MAC_ADDRESS, wrongSecret);
        Device device = createValidDevice();

        when(deviceRepository.findByMacAddress(MAC_ADDRESS)).thenReturn(Optional.of(device));

        // WHEN & THEN
        assertThrows(InvalidDeviceCredentialsException.class, () -> deviceService.pairDevice(USER_ID, request));

        verify(userRepository, never()).findById(anyLong());
        verify(deviceMapper, never()).toDeviceDetailsResponse(any());
    }

    @Test
    void pairDevice_ShouldThrowResourceInUseException_WhenDeviceAlreadyHasUser() {
        // GIVEN
        DeviceCredentialsRequest request = new DeviceCredentialsRequest(MAC_ADDRESS, DEVICE_SECRET);

        Device device = createValidDevice();
        device.setUser(new User()); // User assigned

        when(deviceRepository.findByMacAddress(MAC_ADDRESS)).thenReturn(Optional.of(device));

        // WHEN & THEN
        assertThrows(ResourceInUseException.class, () -> deviceService.pairDevice(USER_ID, request),
                "Expected ResourceInUseException, because the device was already paired with a user");

        verify(userRepository, never()).findById(anyLong());
        verify(deviceMapper, never()).toDeviceDetailsResponse(any());
    }

    @Test
    void pairDevice_ShouldPairSuccessfully_WhenDataIsValid() {
        // GIVEN
        DeviceCredentialsRequest request = new DeviceCredentialsRequest(MAC_ADDRESS, DEVICE_SECRET);
        DeviceDetailsResponse response = new DeviceDetailsResponse(
                DEVICE_ID,
                MAC_ADDRESS,
                "Room",
                false
        );

        Device device = createValidDevice();
        device.setUser(null);

        User user = new User();
        user.setId(USER_ID);

        when(deviceRepository.findByMacAddress(MAC_ADDRESS)).thenReturn(Optional.of(device));
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
        when(deviceMapper.toDeviceDetailsResponse(device)).thenReturn(response);

        // WHEN
        DeviceDetailsResponse actualResponse = deviceService.pairDevice(USER_ID, request);

        // THEN
        assertNotNull(actualResponse);
        assertEquals(response, actualResponse);
        assertEquals(user, device.getUser(), "User should be assigned to a device");

        verify(userRepository, times(1)).findById(USER_ID);
        verify(deviceMapper, times(1)).toDeviceDetailsResponse(device);
    }

    @Test
    void disconnectDevice_ShouldThrowItemNotFoundException_WhenUserDoesNotExist() {
        // GIVEN
        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(ItemNotFoundException.class, () -> deviceService.disconnectDevice(USER_ID, DEVICE_ID));

        verify(deviceRepository, never()).findById(anyLong());
    }

    private Device createValidDevice() {
        Device device = new Device();
        device.setDeviceSecret(CryptoUtils.hashDeviceSecret(DEVICE_SECRET));
        return device;
    }
}
