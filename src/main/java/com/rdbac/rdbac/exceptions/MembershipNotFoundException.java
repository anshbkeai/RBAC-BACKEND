package com.rdbac.rdbac.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MembershipNotFoundException extends RuntimeException {
    public MembershipNotFoundException() { super("Membership not found for user in organization"); }
    public MembershipNotFoundException(String message) { super(message); }
    public MembershipNotFoundException(String message, Throwable cause) { super(message, cause); }
}
