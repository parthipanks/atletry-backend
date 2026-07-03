package com.atletry.service;

import com.atletry.config.AtletryProperties;
import com.atletry.entity.OtpRecord;
import com.atletry.exception.BadRequestException;
import com.atletry.repository.OtpRecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.ZonedDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class OtpService {

    private final OtpRecordRepository otpRepo;
    private final AtletryProperties   props;
    private final SmsProvider         smsProvider;

    private final SecureRandom random = new SecureRandom();

    /** Generates, persists and sends an OTP via the configured SMS provider. Returns the code. */
    @Transactional
    public String generateAndSend(String mobile) {
        otpRepo.invalidateAllByMobile(mobile);

        String otp = props.getOtp().isMockEnabled()
                ? props.getOtp().getMockValue()
                : generate(props.getOtp().getLength());

        otpRepo.save(OtpRecord.builder()
                .mobile(mobile)
                .otpCode(otp)
                .expiresAt(ZonedDateTime.now().plusMinutes(props.getOtp().getExpiryMinutes()))
                .build());

        smsProvider.send(mobile, otp);
        log.info("OTP dispatched via [{}] to {}", smsProvider.getProviderName(), mask(mobile));

        return otp;
    }

    /** Validates the OTP. Throws BadRequestException on failure. */
    @Transactional
    public void verify(String mobile, String otp) {
        OtpRecord record = otpRepo
                .findTopByMobileAndIsUsedFalseAndExpiresAtAfterOrderByCreatedDateDesc(mobile, ZonedDateTime.now())
                .orElseThrow(() -> new BadRequestException("OTP expired or not found. Please request a new one."));

        if (!record.getOtpCode().equals(otp)) {
            throw new BadRequestException("Invalid OTP. Please try again.");
        }

        record.setUsed(true);
        otpRepo.save(record);
    }

    // ── helpers ──────────────────────────────────────────────────────────────

    private String generate(int length) {
        int bound = (int) Math.pow(10, length);
        return String.format("%0" + length + "d", random.nextInt(bound));
    }

    private String mask(String mobile) {
        if (mobile == null || mobile.length() < 6) return "****";
        return mobile.substring(0, 2) + "****" + mobile.substring(mobile.length() - 2);
    }
}
