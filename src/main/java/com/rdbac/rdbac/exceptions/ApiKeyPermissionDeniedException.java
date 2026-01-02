package com.rdbac.rdbac.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ApiKeyPermissionDeniedException extends RuntimeException {
    public ApiKeyPermissionDeniedException() { super("Not allowed to perform this API key operation"); }
    public ApiKeyPermissionDeniedException(String message) { super(message); }
    public ApiKeyPermissionDeniedException(String message, Throwable cause) { super(message, cause); }
}
