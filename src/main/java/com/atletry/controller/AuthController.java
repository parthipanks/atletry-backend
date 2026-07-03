package com.atletry.controller;

import com.atletry.dto.request.SendOtpRequest;
import com.atletry.dto.request.VerifyOtpRequest;
import com.atletry.dto.response.ApiResponse;
import com.atletry.dto.response.AuthResponse;
import com.atletry.dto.response.SendOtpResponse;
import com.atletry.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "1. Authentication", description = "Mobile OTP login — no token required")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/send-otp")
    @Operation(
        summary = "Send OTP",
        description = """
            Sends a 6-digit OTP to the provided mobile number.

            **Dev/mock mode:** `devOtp` is returned in the response so you can test without SMS.
            Set `OTP_MOCK_ENABLED=false` and wire an SMS provider for production.
            """
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OTP sent"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid mobile number")
    })
    public ResponseEntity<ApiResponse<SendOtpResponse>> sendOtp(
            @Valid @RequestBody SendOtpRequest req) {
        return ResponseEntity.ok(ApiResponse.ok("OTP sent successfully", authService.sendOtp(req)));
    }

    @PostMapping("/verify-otp")
    @Operation(
        summary = "Verify OTP & get JWT",
        description = """
            Verifies the OTP and returns a JWT access token.

            **Client routing logic based on response:**
            | `newUser` | `profileComplete` | Action |
            |-----------|-------------------|--------|
            | `true`    | `false`           | → Profile setup screen (name + city) |
            | `false`   | `false`           | → Sport selection screen |
            | `false`   | `true`            | → Home screen |
            """
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Authenticated — token returned"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid or expired OTP")
    })
    public ResponseEntity<ApiResponse<AuthResponse>> verifyOtp(
            @Valid @RequestBody VerifyOtpRequest req) {
        return ResponseEntity.ok(ApiResponse.ok("Login successful", authService.verifyOtp(req)));
    }
}
