package com.InfoSec.dynamic_password.global.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StatusResponseDto {
    private Integer status;
    private String message;
    private Object data;

    public StatusResponseDto(Integer status) {
        this.status = status;
    }

    public StatusResponseDto(Integer status, String message) {}

    public StatusResponseDto(Integer status, Object data) {}

    public static StatusResponseDto addStatus(Integer status) {
        return new StatusResponseDto(status);
    }

    public static StatusResponseDto success() {
        return new StatusResponseDto(200);
    }

    public static StatusResponseDto success(Object data) {
        return new StatusResponseDto(200,null ,data);
    }

    public static StatusResponseDto success(Object data, String message) {return new StatusResponseDto(200, message, data);}

    public static StatusResponseDto success(String message) {return new StatusResponseDto(200, message);}
}
