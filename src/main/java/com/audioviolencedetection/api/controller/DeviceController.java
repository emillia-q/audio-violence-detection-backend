package com.audioviolencedetection.api.controller;

import com.audioviolencedetection.api.dto.response.DeviceDetailsResponse;
import com.audioviolencedetection.api.dto.response.DeviceListResponse;
import com.audioviolencedetection.api.security.model.SecurityUser;
import com.audioviolencedetection.api.service.DeviceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    @Operation(summary = "Return information about device to user")
    @ApiResponse(responseCode = "200", description = "Returns information about the device")
    @ApiResponse(responseCode = "404", description = "Device not found")
    public DeviceDetailsResponse getDeviceDetails(@AuthenticationPrincipal SecurityUser securityUser,
                                                  @PathVariable("id") Long deviceId) {
        return deviceService.getDeviceDetails(securityUser.getId(), deviceId);
    }
}
