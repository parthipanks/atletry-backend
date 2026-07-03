package com.atletry.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data @Builder @NoArgsConstructor @AllArgsConstructor
@Schema(description = "JWT token response after successful OTP verification")
public class AuthResponse {

    @Schema(description = "JWT access token — attach as 'Authorization: Bearer <token>'")
    private String accessToken;

    @Schema(example = "Bearer")
    private String tokenType;

    @Schema(description = "Token expiry in milliseconds from now", example = "86400000")
    private long expiresIn;

    @Schema(description = "Authenticated user's ID")
    private Long userId;

    @Schema(description = "true = brand-new user; show profile setup screen")
    private boolean newUser;

    @Schema(description = "false = name/city not yet set; false = sports not yet selected")
    private boolean profileComplete;
}
