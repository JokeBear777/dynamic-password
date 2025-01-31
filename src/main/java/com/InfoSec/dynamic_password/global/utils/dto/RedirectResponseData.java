package com.InfoSec.dynamic_password.global.utils.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RedirectResponseData {
    private String message;
    private String redirectUrl;
}
