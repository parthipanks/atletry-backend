package com.atletry.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "atletry")
@Getter @Setter
public class AtletryProperties {

    private Jwt        jwt        = new Jwt();
    private Otp        otp        = new Otp();
    private Aws        aws        = new Aws();
    private Sms        sms        = new Sms();
    private Firebase   firebase   = new Firebase();
    private SuperAdmin superAdmin = new SuperAdmin();

    @Getter @Setter
    public static class SuperAdmin {
        /** Mobile number of the bootstrap super-admin (e.g. +919999999999). */
        private String mobile = "";
    }

    @Getter @Setter
    public static class Jwt {
        private String secret;
        private long   accessTokenExpiryMs;
        private long   refreshTokenExpiryMs;
    }

    @Getter @Setter
    public static class Otp {
        private int     expiryMinutes;
        private int     length;
        private boolean mockEnabled;
        private String  mockValue;
    }

    @Getter @Setter
    public static class Aws {
        private String region;
        private S3     s3 = new S3();

        @Getter @Setter
        public static class S3 {
            private String bucket;
            private String accessKey;
            private String secretKey;
        }
    }

    @Getter @Setter
    public static class Sms {
        /** Active provider: mock | twilio */
        private String provider = "mock";
        private Twilio twilio   = new Twilio();

        @Getter @Setter
        public static class Twilio {
            private String accountSid;
            private String authToken;
            private String fromNumber;
            private String messageTemplate =
                    "Your Atletry OTP is {otp}. Valid for a few minutes. Do not share it.";
        }
    }

    @Getter @Setter
    public static class Firebase {
        /** Path to the Firebase service account JSON key file */
        private String serviceAccountPath = "";
        /** Set to true to enable FCM push notifications */
        private boolean enabled = false;
    }
}
