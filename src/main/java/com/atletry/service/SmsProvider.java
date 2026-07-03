package com.atletry.service;

public interface SmsProvider {

    /**
     * Sends the OTP to the given 10-digit Indian mobile number.
     * Implementations must not throw — failures should be wrapped in a RuntimeException.
     */
    void send(String mobile, String otp);

    String getProviderName();
}
