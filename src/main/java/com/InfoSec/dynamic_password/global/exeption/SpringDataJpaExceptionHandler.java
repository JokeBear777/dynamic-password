package com.InfoSec.dynamic_password.global.exeption;

import com.InfoSec.dynamic_password.global.utils.dto.MessageStatusResponseDto;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class SpringDataJpaExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFoundException(EntityNotFoundException e) {
        MessageStatusResponseDto response = new MessageStatusResponseDto(
                HttpStatus.NOT_FOUND.value(),
                "Requested resource was not found.");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        MessageStatusResponseDto response = new MessageStatusResponseDto(
                HttpStatus.CONFLICT.value(),
                "Data integrity violation occurred. Please check database constraints.");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }
}
