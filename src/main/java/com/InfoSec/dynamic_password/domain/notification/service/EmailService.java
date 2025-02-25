package com.InfoSec.dynamic_password.domain.notification.service;

import org.springframework.stereotype.Service;

public interface EmailService {
    public void sendEmail(String to, String subject, String body, String html);
    public void sendOtpFailEmail(Long memberId);
}
