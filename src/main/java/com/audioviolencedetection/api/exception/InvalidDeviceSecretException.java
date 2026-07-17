package com.audioviolencedetection.api.exception;

public class InvalidDeviceSecretException extends RuntimeException {
    public InvalidDeviceSecretException(String message) {
        super(message);
    }
}
