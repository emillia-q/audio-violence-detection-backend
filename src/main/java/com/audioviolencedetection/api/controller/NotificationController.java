package com.audioviolencedetection.api.controller;

import com.audioviolencedetection.api.dto.response.NotificationListResponse;
import com.audioviolencedetection.api.security.model.SecurityUser;
import com.audioviolencedetection.api.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/notifications")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@PreAuthorize("hasRole('USER')")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    @Operation(summary = "Get list of all protected users notifications")
    @ApiResponse(responseCode = "200", description = "Return list of notifications")
    @ApiResponse(responseCode = "204", description = "List of notifications is empty")
    public ResponseEntity<List<NotificationListResponse>> getProtectedUsersNotifications(
            @AuthenticationPrincipal SecurityUser securityUser) {
        List<NotificationListResponse> notifications = notificationService.getProtectedUsersNotifications(securityUser.getId());

        if (notifications.isEmpty())
            return ResponseEntity.noContent().build();

        return ResponseEntity.ok(notifications);
    }

    @PatchMapping(path = "/{id}/toggle-status")
    @Operation(summary = "Toggle notification status")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponse(responseCode = "204", description = "Notification status changed")
    @ApiResponse(responseCode = "404", description = "Notification not found")
    public void toggleNotificationStatus(@AuthenticationPrincipal SecurityUser securityUser,
                                         @PathVariable("id") Long notificationId) {
        notificationService.toggleNotificationStatus(securityUser.getId(), notificationId);
    }
}
