package com.audioviolencedetection.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidDeviceCredentialsException extends RuntimeException {
    public InvalidDeviceCredentialsException(String message) {
        super(message);
    }
}
