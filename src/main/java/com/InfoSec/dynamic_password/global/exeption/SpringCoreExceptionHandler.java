package com.InfoSec.dynamic_password.global.exeption;

import com.InfoSec.dynamic_password.global.utils.dto.MessageStatusResponseDto;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class SpringCoreExceptionHandler {
    @ExceptionHandler(NoSuchBeanDefinitionException.class)
    public ResponseEntity<?> handleNoSuchBeanDefinitionException(NoSuchBeanDefinitionException e) {
        MessageStatusResponseDto response = new MessageStatusResponseDto(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Requested bean is not found in the Spring context.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(BeanCreationException.class)
    public ResponseEntity<?> handleBeanCreationException(BeanCreationException e) {
        MessageStatusResponseDto response = new MessageStatusResponseDto(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Error occurred while creating a Spring bean. Check configurations.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
