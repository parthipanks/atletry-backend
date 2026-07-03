package com.atletry.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(name = "atletry.sms.provider", havingValue = "mock", matchIfMissing = true)
public class MockSmsProvider implements SmsProvider {

    @Override
    public void send(String mobile, String otp) {
        log.debug("[MOCK SMS] {} ← OTP: {}", mask(mobile), otp);
    }

    @Override
    public String getProviderName() {
        return "mock";
    }

    private String mask(String mobile) {
        if (mobile == null || mobile.length() < 6) return "****";
        return mobile.substring(0, 2) + "****" + mobile.substring(mobile.length() - 2);
    }
}
