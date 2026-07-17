package com.audioviolencedetection.api.security.filter;

import com.audioviolencedetection.api.entity.Device;
import com.audioviolencedetection.api.security.model.SecurityDevice;
import com.audioviolencedetection.api.security.model.SecurityUser;
import com.audioviolencedetection.api.security.service.JwtService;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @Nonnull HttpServletRequest request,
            @Nonnull HttpServletResponse response,
            @Nonnull FilterChain filterChain
            ) throws ServletException, IOException {

        // Extract and clear the token from Bearer
        jwtService.getTokenFromRequest(request).ifPresent(token -> {
            String userName = jwtService.extractUsername(token);
            String userType = jwtService.extractUserType(token);

            // If the userName exists and the user/device is not yet logged in in this HTTP request
            if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails;

                if ("device".equals(userType)) {
                    Long deviceId = jwtService.extractDeviceId(token);

                    Device device = Device.builder()
                            .id(deviceId)
                            .macAddress(userName)
                            .isActivated(true)
                            .build();
                    userDetails = new SecurityDevice(device);
                } else {
                    // Load user data by extracting it from the database via userName
                    userDetails = this.userDetailsService.loadUserByUsername(userName);
                }

                // Check whether token matches user from the database and it has not expired
                if (jwtService.isTokenValid(token, userDetails)) {
                    // Create a pass
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // Throw a pass into the Spring Security context
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        });
        // Despite result, pass the request
        filterChain.doFilter(request, response);
    }
}
