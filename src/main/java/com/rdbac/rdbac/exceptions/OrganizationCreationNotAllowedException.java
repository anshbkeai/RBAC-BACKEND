package com.rdbac.rdbac.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class OrganizationCreationNotAllowedException extends RuntimeException {
    public OrganizationCreationNotAllowedException() { super("User is not allowed to create more organizations"); }
    public OrganizationCreationNotAllowedException(String message) { super(message); }
    public OrganizationCreationNotAllowedException(String message, Throwable cause) { super(message, cause); }
}
