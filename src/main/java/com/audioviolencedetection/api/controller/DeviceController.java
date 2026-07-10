package com.audioviolencedetection.api.controller;

import com.audioviolencedetection.api.dto.response.DeviceListResponse;
import com.audioviolencedetection.api.security.model.SecurityUser;
import com.audioviolencedetection.api.service.DeviceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/devices")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
public class DeviceController {

    private final DeviceService deviceService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Return a list of user devices")
    @ApiResponse(responseCode = "200", description = "Returns a list of all user devices")
    public List<DeviceListResponse> getUserDevices(@AuthenticationPrincipal SecurityUser securityUser) {
        return deviceService.getUserDevices(securityUser.getId());
    }

}
