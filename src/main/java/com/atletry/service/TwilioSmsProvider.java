package com.atletry.service;

import com.atletry.config.AtletryProperties;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(name = "atletry.sms.provider", havingValue = "twilio")
@RequiredArgsConstructor
public class TwilioSmsProvider implements SmsProvider {

    private final AtletryProperties props;

    @PostConstruct
    void init() {
        AtletryProperties.Sms.Twilio cfg = props.getSms().getTwilio();
        Twilio.init(cfg.getAccountSid(), cfg.getAuthToken());
        log.info("Twilio SMS provider initialized (from={})", cfg.getFromNumber());
    }

    @Override
    public void send(String mobile, String otp) {
        AtletryProperties.Sms.Twilio cfg = props.getSms().getTwilio();
        String body = cfg.getMessageTemplate().replace("{otp}", otp);

        Message.creator(
                new PhoneNumber("+91" + mobile),
                new PhoneNumber(cfg.getFromNumber()),
                body
        ).create();

        log.info("[TWILIO] SMS dispatched to {}", mask(mobile));
    }

    @Override
    public String getProviderName() {
        return "twilio";
    }

    private String mask(String mobile) {
        if (mobile == null || mobile.length() < 6) return "****";
        return mobile.substring(0, 2) + "****" + mobile.substring(mobile.length() - 2);
    }
}
