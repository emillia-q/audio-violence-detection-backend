package com.audioviolencedetection.api.controller;

import com.audioviolencedetection.api.security.model.SecurityDevice;
import com.audioviolencedetection.api.service.AlertService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/alerts")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
public class AlertController {

    private final AlertService alertService;

    @PostMapping
    @PreAuthorize("hasRole('DEVICE')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Send alert about detected threat to database by the device")
    @ApiResponse(responseCode = "204", description = "Alert sent successfully")
    @ApiResponse(responseCode = "401", description = "Unauthorized: token possibly expired")
    @ApiResponse(responseCode = "403", description = "Forbidden: Required role 'DEVICE' is missing")
    @ApiResponse(responseCode = "404", description = "Device not found")
    @ApiResponse(responseCode = "422", description = "User not assigned to the device")
    public void sendAlertToDatabase(@AuthenticationPrincipal SecurityDevice securityDevice) {
        alertService.sendAlertToDatabase(securityDevice.getId());
    }
}
