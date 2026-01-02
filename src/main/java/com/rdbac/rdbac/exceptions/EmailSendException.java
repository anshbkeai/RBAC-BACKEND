package com.rdbac.rdbac.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class EmailSendException extends RuntimeException {
    public EmailSendException() { super("Failed to send email"); }
    public EmailSendException(String message) { super(message); }
    public EmailSendException(String message, Throwable cause) { super(message, cause); }
}
