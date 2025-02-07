package com.InfoSec.dynamic_password.global.exeption;

import com.InfoSec.dynamic_password.global.utils.dto.MessageStatusResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class DatabaseTransactionExceptionHandler {
    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity<?> handleTransactionSystemException(TransactionSystemException e) {
        MessageStatusResponseDto response = new MessageStatusResponseDto(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Transaction error occurred. Please try again.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
