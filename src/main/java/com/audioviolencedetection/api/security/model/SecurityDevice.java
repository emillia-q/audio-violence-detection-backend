package com.audioviolencedetection.api.security.model;

import com.audioviolencedetection.api.entity.Device;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class SecurityDevice implements UserDetails {
    private final Device device;

    public SecurityDevice(Device device) {
        this.device = device;
    }

    // Device get token after verification of device secret in AuthService
    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return device.getMacAddress();
    }

    public Long getId() {
        return device.getId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_DEVICE"));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // Device is active if it's paired with user
    @Override
    public boolean isEnabled() {
        return device.getIsActivated();
    }
}
