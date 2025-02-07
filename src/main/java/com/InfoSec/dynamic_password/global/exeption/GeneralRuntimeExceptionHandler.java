package com.InfoSec.dynamic_password.global.exeption;

import com.InfoSec.dynamic_password.global.utils.dto.MessageStatusResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GeneralRuntimeExceptionHandler {
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<?> handleNullPointerException(NullPointerException e) {
        MessageStatusResponseDto response = new MessageStatusResponseDto(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "A required value was missing or null. Please check your input.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException e) {
        MessageStatusResponseDto response = new MessageStatusResponseDto(
                HttpStatus.BAD_REQUEST.value(),
                "Invalid argument: " + e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(IndexOutOfBoundsException.class)
    public ResponseEntity<?> handleIndexOutOfBoundsException(IndexOutOfBoundsException e) {
        MessageStatusResponseDto response = new MessageStatusResponseDto(
                HttpStatus.BAD_REQUEST.value(),
                "Array or list index is out of bounds. Please check your request.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(ArithmeticException.class)
    public ResponseEntity<?> handleArithmeticException(ArithmeticException e) {
        MessageStatusResponseDto response = new MessageStatusResponseDto(
                HttpStatus.BAD_REQUEST.value(),
                "Mathematical error occurred, such as division by zero.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
