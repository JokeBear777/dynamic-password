package com.InfoSec.dynamic_password.global.exeption.custom;

public class RedisOperationException extends RuntimeException {
    public RedisOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
