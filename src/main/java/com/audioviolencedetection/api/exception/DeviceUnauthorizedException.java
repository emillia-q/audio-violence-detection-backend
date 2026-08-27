package com.audioviolencedetection.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class DeviceUnauthorizedException extends RuntimeException {
    public DeviceUnauthorizedException(String message) {
        super(message);
    }
}
