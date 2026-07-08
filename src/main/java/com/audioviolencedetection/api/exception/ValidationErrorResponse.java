package com.audioviolencedetection.api.exception;

import java.time.LocalDateTime;
import java.util.List;

public record ValidationErrorResponse(
        LocalDateTime timestamp,
        int status,
        List<String> errors
) {
}
