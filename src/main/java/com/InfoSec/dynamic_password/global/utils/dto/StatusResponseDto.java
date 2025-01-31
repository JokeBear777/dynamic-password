package com.InfoSec.dynamic_password.global.utils.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class StatusResponseDto extends BaseResponseDto{
    private Integer status;
    private Object data;

    public StatusResponseDto(Integer status) {
        super(status);
    }

    public StatusResponseDto(Integer status, Object data) {
        super(status, data);
    }

    public static StatusResponseDto success() {
        return new StatusResponseDto(200);
    }

    public static StatusResponseDto success(Object data) {
        return new StatusResponseDto(200, data);
    }

    public static StatusResponseDto addStatus(Integer status) {
        return new StatusResponseDto(status);
    }

}
