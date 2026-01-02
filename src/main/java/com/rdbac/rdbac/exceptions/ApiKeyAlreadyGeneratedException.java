package com.rdbac.rdbac.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ApiKeyAlreadyGeneratedException extends RuntimeException {
    public ApiKeyAlreadyGeneratedException() { super("API key already generated for organization"); }
    public ApiKeyAlreadyGeneratedException(String message) { super(message); }
    public ApiKeyAlreadyGeneratedException(String message, Throwable cause) { super(message, cause); }
}
