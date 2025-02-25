package com.InfoSec.dynamic_password.domain.notification.service;

import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
    @Override
    public void sendEmail(String to, String subject, String body, String html) {

    }
    @Override
    public void sendOtpFailEmail(Long memberId) {}
    ;
}
