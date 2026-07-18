package com.audioviolencedetection.api.controller;

import com.audioviolencedetection.api.security.model.SecurityDevice;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/alerts")
public class AlertController {

    @PostMapping
    @PreAuthorize("hasRole('DEVICE')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Send alert about detected threat to database by the device")
    @ApiResponse(responseCode = "204", description = "Alert sent successfully")
    @ApiResponse(responseCode = "401", description = "Unauthorized: token possibly expired")
    public void sendAlertToDatabase(@AuthenticationPrincipal SecurityDevice securityDevice) {

    }

}
