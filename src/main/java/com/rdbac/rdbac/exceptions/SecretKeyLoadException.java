package com.rdbac.rdbac.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class SecretKeyLoadException extends RuntimeException {
    public SecretKeyLoadException() { super("Failed to load secret key"); }
    public SecretKeyLoadException(String message) { super(message); }
    public SecretKeyLoadException(String message, Throwable cause) { super(message, cause); }
}
