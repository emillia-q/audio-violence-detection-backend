package com.audioviolencedetection.api.exception;

public class DeviceUnauthorizedException extends RuntimeException {
    public DeviceUnauthorizedException(String message) {
        super(message);
    }
}
