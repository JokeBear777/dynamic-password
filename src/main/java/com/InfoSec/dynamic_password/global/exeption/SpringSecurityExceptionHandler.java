package com.InfoSec.dynamic_password.global.exeption;

import com.InfoSec.dynamic_password.global.utils.dto.MessageStatusResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.security.sasl.AuthenticationException;

@RestControllerAdvice
public class SpringSecurityExceptionHandler {
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException e) {
        MessageStatusResponseDto response = new MessageStatusResponseDto(
                HttpStatus.FORBIDDEN.value(),
                "Access denied. You do not have permission to perform this action.");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> handleAuthenticationException(AuthenticationException e) {
        MessageStatusResponseDto response = new MessageStatusResponseDto(
                HttpStatus.UNAUTHORIZED.value(),
                "Authentication failed. Please check your credentials.");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
}
