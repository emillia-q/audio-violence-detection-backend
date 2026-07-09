package com.audioviolencedetection.api.controller;

import com.audioviolencedetection.api.dto.request.AddTrustedUserRequest;
import com.audioviolencedetection.api.security.model.SecurityUser;
import com.audioviolencedetection.api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/users")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
public class UserController {

    private final UserService userService;

    @PatchMapping("/trusted-user")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Assign a trusted user to the current user profile")
    @ApiResponse(responseCode = "204", description = "Trusted user set")
    @ApiResponse(responseCode = "400", description = "Invalid request data or validation failed")
    @ApiResponse(responseCode = "404", description = "User not found")
    public void setTrustedUser(@Valid @RequestBody AddTrustedUserRequest request,
                               @AuthenticationPrincipal SecurityUser securityUser) {
        userService.setTrustedUser(request, securityUser.getId());
    }

    @DeleteMapping("/trusted-user")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remove a trusted user from the current user profile")
    @ApiResponse(responseCode = "204", description = "Trusted user deleted")
    @ApiResponse(responseCode = "404", description = "Current user not found")
    public void deleteTrustedUser(@AuthenticationPrincipal SecurityUser securityUser) {
        userService.deleteTrustedUser(securityUser.getId());
    }
}
