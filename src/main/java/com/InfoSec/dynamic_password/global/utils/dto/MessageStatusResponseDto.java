package com.InfoSec.dynamic_password.global.utils.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class MessageStatusResponseDto extends BaseResponseDto {
    private String message;

    public MessageStatusResponseDto(Integer status, String message) {
        super(status);
        this.message = message;
    }

    public MessageStatusResponseDto(Integer status, String message, Object data) {
        super(status, data);
        this.message = message;
    }

    public static MessageStatusResponseDto success(String message) {
        return new MessageStatusResponseDto(200, message);
    }

    public static MessageStatusResponseDto success(String message, Object data) {
        return new MessageStatusResponseDto(200, message, data);
    }
}
