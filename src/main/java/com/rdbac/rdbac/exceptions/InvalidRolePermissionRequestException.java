package com.rdbac.rdbac.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidRolePermissionRequestException extends RuntimeException {
    public InvalidRolePermissionRequestException() { super("Either role or permission must be provided"); }
    public InvalidRolePermissionRequestException(String message) { super(message); }
    public InvalidRolePermissionRequestException(String message, Throwable cause) { super(message, cause); }
}
