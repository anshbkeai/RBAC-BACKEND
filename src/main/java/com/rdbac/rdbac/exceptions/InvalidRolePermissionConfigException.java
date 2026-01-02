package com.rdbac.rdbac.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidRolePermissionConfigException extends RuntimeException {
    public InvalidRolePermissionConfigException() { super("Invalid roles/permissions configuration"); }
    public InvalidRolePermissionConfigException(String message) { super(message); }
    public InvalidRolePermissionConfigException(String message, Throwable cause) { super(message, cause); }
}
