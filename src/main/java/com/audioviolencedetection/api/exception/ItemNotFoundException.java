package com.audioviolencedetection.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ItemNotFoundException extends RuntimeException {

    private ItemNotFoundException(Class<?> entityClass, String fieldName, Object value) {
        super(String.format("%s with %s: '%s' not found", entityClass.getSimpleName(), fieldName, value));
    }

    // Id
    public static ItemNotFoundException createForId(Class<?> entityClass, Object id) {
        return new ItemNotFoundException(entityClass, "ID", id);
    }

    // Email
    public static ItemNotFoundException createForEmail(Class<?> entityClass, String email) {
        return new ItemNotFoundException(entityClass, "email", email);
    }
}
