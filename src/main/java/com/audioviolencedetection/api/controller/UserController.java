package com.audioviolencedetection.api.controller;

import com.audioviolencedetection.api.dto.request.AddTrustedUserRequest;
import com.audioviolencedetection.api.security.model.SecurityUser;
import com.audioviolencedetection.api.service.UserService;
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
    public void setTrustedUser(@Valid @RequestBody AddTrustedUserRequest request,
                               @AuthenticationPrincipal SecurityUser securityUser) {
        userService.setTrustedUser(request, securityUser.getId());
    }
}
