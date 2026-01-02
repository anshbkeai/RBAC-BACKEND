package com.rdbac.rdbac.exceptions;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import io.jsonwebtoken.ExpiredJwtException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiError> handleUserNotFoundException(UserNotFoundException ex, WebRequest request) {
        ApiError error = new ApiError(
            ex.getMessage(),
            "USER_NOT_FOUND",
            request.getDescription(false).replace("uri=", ""),
            LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(OrganizationNotFoundException.class)
    public ResponseEntity<ApiError> handleOrganizationNotFoundException(OrganizationNotFoundException ex, WebRequest request) {
        ApiError error = new ApiError(
            ex.getMessage(),
            "ORGANIZATION_NOT_FOUND",
            request.getDescription(false).replace("uri=", ""),
            LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MembershipNotFoundException.class)
    public ResponseEntity<ApiError> handleMembershipNotFoundException(MembershipNotFoundException ex, WebRequest request) {
        ApiError error = new ApiError(
            ex.getMessage(),
            "MEMBERSHIP_NOT_FOUND",
            request.getDescription(false).replace("uri=", ""),
            LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ApiError> handleUserAlreadyExistsException(UserAlreadyExistsException ex, WebRequest request) {
        ApiError error = new ApiError(
            ex.getMessage(),
            "USER_ALREADY_EXISTS",
            request.getDescription(false).replace("uri=", ""),
            LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ApiKeyAlreadyGeneratedException.class)
    public ResponseEntity<ApiError> handleApiKeyAlreadyGeneratedException(ApiKeyAlreadyGeneratedException ex, WebRequest request) {
        ApiError error = new ApiError(
            ex.getMessage(),
            "API_KEY_ALREADY_GENERATED",
            request.getDescription(false).replace("uri=", ""),
            LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AuthenticationFailedException.class)
    public ResponseEntity<ApiError> handleAuthenticationFailedException(AuthenticationFailedException ex, WebRequest request) {
        ApiError error = new ApiError(
            ex.getMessage(),
            "AUTHENTICATION_FAILED",
            request.getDescription(false).replace("uri=", ""),
            LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ApiKeyPermissionDeniedException.class)
    public ResponseEntity<ApiError> handleApiKeyPermissionDeniedException(ApiKeyPermissionDeniedException ex, WebRequest request) {
        ApiError error = new ApiError(
            ex.getMessage(),
            "API_KEY_PERMISSION_DENIED",
            request.getDescription(false).replace("uri=", ""),
            LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(EmailSendException.class)
    public ResponseEntity<ApiError> handleEmailSendException(EmailSendException ex, WebRequest request) {
        ApiError error = new ApiError(
            ex.getMessage(),
            "EMAIL_SEND_FAILED",
            request.getDescription(false).replace("uri=", ""),
            LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(SecretKeyLoadException.class)
    public ResponseEntity<ApiError> handleSecretKeyLoadException(SecretKeyLoadException ex, WebRequest request) {
        ApiError error = new ApiError(
            ex.getMessage(),
            "SECRET_KEY_LOAD_FAILED",
            request.getDescription(false).replace("uri=", ""),
            LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidRolePermissionConfigException.class)
    public ResponseEntity<ApiError> handleInvalidRolePermissionConfigException(InvalidRolePermissionConfigException ex, WebRequest request) {
        ApiError error = new ApiError(
            ex.getMessage(),
            "INVALID_ROLE_PERMISSION_CONFIG",
            request.getDescription(false).replace("uri=", ""),
            LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidRolePermissionRequestException.class)
    public ResponseEntity<ApiError> handleInvalidRolePermissionRequestException(InvalidRolePermissionRequestException ex, WebRequest request) {
        ApiError error = new ApiError(
            ex.getMessage(),
            "INVALID_ROLE_PERMISSION_REQUEST",
            request.getDescription(false).replace("uri=", ""),
            LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OrganizationCreationNotAllowedException.class)
    public ResponseEntity<ApiError> handleOrganizationCreationNotAllowedException(OrganizationCreationNotAllowedException ex, WebRequest request) {
        ApiError error = new ApiError(
            ex.getMessage(),
            "ORGANIZATION_CREATION_NOT_ALLOWED",
            request.getDescription(false).replace("uri=", ""),
            LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ApiError> handleInvlidJwt(ExpiredJwtException inValidJWT , WebRequest request){
        ApiError apiError = new ApiError(
            inValidJWT.getMessage(),
            HttpStatus.UNAUTHORIZED.toString(),
            request.getDescription(false).replace("uri=", ""),
            LocalDateTime.now()
        );

        return new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGlobalException(Exception ex, WebRequest request) {
        ApiError error = new ApiError(
            "An unexpected error occurred",
            "INTERNAL_SERVER_ERROR",
            request.getDescription(false).replace("uri=", ""),
            LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    
}

