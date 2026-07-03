package com.atletry.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "OTP send result")
public class SendOtpResponse {

    @Schema(description = "Mobile number with middle digits masked", example = "98****3210")
    private String maskedMobile;

    @Schema(description = "How many minutes the OTP is valid", example = "5")
    private int expiryMinutes;

    @Schema(description = "Only present in mock/dev mode — never exposed in production")
    private String devOtp;
}
