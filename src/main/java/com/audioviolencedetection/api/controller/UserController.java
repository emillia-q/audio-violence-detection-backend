package com.audioviolencedetection.api.controller;

import com.audioviolencedetection.api.dto.request.AddTrustedUserRequest;
import com.audioviolencedetection.api.dto.request.ChangeNicknameRequest;
import com.audioviolencedetection.api.dto.response.TrustedUserDetailsResponse;
import com.audioviolencedetection.api.dto.response.TrustedUserListResponse;
import com.audioviolencedetection.api.security.model.SecurityUser;
import com.audioviolencedetection.api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/users")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
public class UserController {

    private final UserService userService;

    @GetMapping("/trusted-users")
    @Operation(summary = "Get list of trusted users for user")
    @ApiResponse(responseCode = "200", description = "Return trusted users list")
    @ApiResponse(responseCode = "204", description = "No trusted user assigned to this account")
    @ApiResponse(responseCode = "404", description = "User not found")
    public ResponseEntity<List<TrustedUserListResponse>> getListOfTrustedUsers(@AuthenticationPrincipal SecurityUser securityUser) {
        List<TrustedUserListResponse> trustedUsers = userService.getListOfTrustedUsers(securityUser.getId());

        if (trustedUsers.isEmpty())
            return ResponseEntity.noContent().build();

        return ResponseEntity.ok(trustedUsers);
    }

    @GetMapping("/trusted-users/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get trusted user details")
    @ApiResponse(responseCode = "200", description = "Return trusted user details")
    @ApiResponse(responseCode = "404", description = "User or relationship not found")
    public TrustedUserDetailsResponse getTrustedUser(@AuthenticationPrincipal SecurityUser securityUser,
                                                                     @PathVariable("id") Long trustedUserId) {
        return userService.getTrustedUser(securityUser.getId(), trustedUserId);
    }

    @PostMapping("/trusted-users")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add new relation, assign trusted user")
    @ApiResponse(responseCode = "201", description = "Trusted user added")
    @ApiResponse(responseCode = "400", description = "Invalid request data or validation failed")
    @ApiResponse(responseCode = "404", description = "User not found")
    @ApiResponse(responseCode = "409", description = "User has already assigned this trusted user")
    public TrustedUserDetailsResponse addTrustedUser(@Valid @RequestBody AddTrustedUserRequest request,
                               @AuthenticationPrincipal SecurityUser securityUser) {
        return userService.addTrustedUser(request, securityUser.getId());
    }

    @PatchMapping("/trusted-users/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Change trusted user nickname")
    @ApiResponse(responseCode = "200", description = "Trusted user nickname changed successfully")
    @ApiResponse(responseCode = "404", description = "Trusted user not found")
    public TrustedUserDetailsResponse changeTrustedUserNickname(@PathVariable("id") Long trustedUserId,
            @Valid @RequestBody ChangeNicknameRequest request,
            @AuthenticationPrincipal SecurityUser securityUser) {

    }

    @DeleteMapping("/trusted-users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remove a trusted user from the current user profile")
    @ApiResponse(responseCode = "204", description = "Trusted user deleted")
    @ApiResponse(responseCode = "404", description = "User, trusted user or relationship not found")
    public void deleteTrustedUser(@AuthenticationPrincipal SecurityUser securityUser,
                                  @PathVariable("id") Long trustedUserId) {
        userService.deleteTrustedUser(securityUser.getId(), trustedUserId);
    }
}
