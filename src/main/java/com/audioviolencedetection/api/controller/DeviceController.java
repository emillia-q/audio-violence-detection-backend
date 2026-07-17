package com.audioviolencedetection.api.controller;

import com.audioviolencedetection.api.dto.request.DeviceActivationRequest;
import com.audioviolencedetection.api.dto.request.UpdateDeviceNameRequest;
import com.audioviolencedetection.api.dto.response.DeviceDetailsResponse;
import com.audioviolencedetection.api.dto.response.DeviceListResponse;
import com.audioviolencedetection.api.security.model.SecurityUser;
import com.audioviolencedetection.api.service.DeviceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/devices")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
public class DeviceController {

    private final DeviceService deviceService;

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Return a list of user devices")
    @ApiResponse(responseCode = "200", description = "Returns a list of all user devices")
    @ApiResponse(responseCode = "204", description = "User has no devices")
    @ApiResponse(responseCode = "404", description = "User not found")
    public ResponseEntity<List<DeviceListResponse>> getUserDevices(@AuthenticationPrincipal SecurityUser securityUser) {
        List<DeviceListResponse> devices = deviceService.getUserDevices(securityUser.getId());

        if (devices.isEmpty())
            return ResponseEntity.noContent().build();

        return ResponseEntity.ok(devices);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Return information about device to user")
    @ApiResponse(responseCode = "200", description = "Returns information about the device")
    @ApiResponse(responseCode = "404", description = "Device not found")
    public DeviceDetailsResponse getDeviceDetails(@AuthenticationPrincipal SecurityUser securityUser,
                                                  @PathVariable("id") Long deviceId) {
        return deviceService.getDeviceDetails(securityUser.getId(), deviceId);
    }

    @PostMapping("/activate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Activate and pair an IoT device with a user account")
    @ApiResponse(responseCode = "204", description = "Activated and paired an IoT device")
    @ApiResponse(responseCode = "400", description = "Invalid request payload or validation failed")
    @ApiResponse(responseCode = "401", description = "Invalid device secret")
    @ApiResponse(responseCode = "404", description = "Device or user not found")
    @ApiResponse(responseCode = "409", description = "Device is already assigned to a user")
    public void activateAndPairDevice(@Valid @RequestBody DeviceActivationRequest request) {
        deviceService.activateAndPairDevice(request);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update device name")
    @ApiResponse(responseCode = "200", description = "Device name updated")
    @ApiResponse(responseCode = "404", description = "Device not found")
    public DeviceDetailsResponse updateDeviceName(@AuthenticationPrincipal SecurityUser securityUser,
                                                  @PathVariable("id") Long deviceId,
                                                  @Valid @RequestBody UpdateDeviceNameRequest request) {
        return deviceService.updateDeviceName(securityUser.getId(), deviceId, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Disconnect the device from a user")
    @ApiResponse(responseCode = "204", description = "Device disconnected")
    @ApiResponse(responseCode = "404", description = "Device not found")
    public void disconnectDevice(@AuthenticationPrincipal SecurityUser securityUser,
                                 @PathVariable("id") Long deviceId) {
        deviceService.disconnectDevice(securityUser.getId(), deviceId);
    }
}
