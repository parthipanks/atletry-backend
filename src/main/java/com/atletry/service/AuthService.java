package com.atletry.service;


import com.atletry.config.AtletryProperties;
import com.atletry.dto.request.SendOtpRequest;
import com.atletry.dto.request.VerifyOtpRequest;
import com.atletry.dto.response.AuthResponse;
import com.atletry.dto.response.SendOtpResponse;
import com.atletry.entity.Role;
import com.atletry.entity.User;
import com.atletry.enums.RoleName;
import com.atletry.repository.RoleRepository;
import com.atletry.repository.UserRepository;
import com.atletry.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final OtpService        otpService;
    private final UserRepository    userRepo;
    private final RoleRepository    roleRepo;
    private final JwtUtil           jwtUtil;
    private final AtletryProperties props;

    @Transactional
    public SendOtpResponse sendOtp(SendOtpRequest req) {
        String otp     = otpService.generateAndSend(req.getMobile());
        String masked  = mask(req.getMobile());

        SendOtpResponse res = SendOtpResponse.builder()
                .maskedMobile(masked)
                .expiryMinutes(props.getOtp().getExpiryMinutes())
                .build();

        if (props.getOtp().isMockEnabled()) {
            res.setDevOtp(otp);
        }
        return res;
    }

    @Transactional
    public AuthResponse verifyOtp(VerifyOtpRequest req) {
        otpService.verify(req.getMobile(), req.getOtp());

        boolean isNewUser = !userRepo.existsByMobile(req.getMobile());

        User user = userRepo.findByMobile(req.getMobile()).orElseGet(() -> {
            Role userRole = roleRepo.findByName(RoleName.USER).orElseThrow();
            User newUser  = User.builder().mobile(req.getMobile()).build();
            newUser.getRoles().add(userRole);
            return userRepo.save(newUser);
        });

        String token = jwtUtil.generateAccessToken(user.getId(), user.getMobile());

        return AuthResponse.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .expiresIn(jwtUtil.accessTokenExpiryMs())
                .userId(user.getId())
                .newUser(isNewUser)
                .profileComplete(user.isProfileComplete())
                .build();
    }

    private String mask(String mobile) {
        return mobile.substring(0, 2) + "****" + mobile.substring(mobile.length() - 4);
    }
}
