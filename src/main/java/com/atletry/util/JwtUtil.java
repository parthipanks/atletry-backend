package com.atletry.util;

import com.atletry.config.AtletryProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final AtletryProperties props;

    private SecretKey signingKey() {
        byte[] raw = props.getJwt().getSecret().getBytes(StandardCharsets.UTF_8);
        byte[] key = new byte[32];
        System.arraycopy(raw, 0, key, 0, Math.min(raw.length, 32));
        return Keys.hmacShaKeyFor(key);
    }

    public String generateAccessToken(Long userId, String mobile) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .subject(userId.toString())
                .claim("mobile", mobile)
                .claim("type", "ACCESS")
                .issuedAt(new Date(now))
                .expiration(new Date(now + props.getJwt().getAccessTokenExpiryMs()))
                .signWith(signingKey())
                .compact();
    }

    public Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Long extractUserId(String token) {
        return Long.parseLong(parseClaims(token).getSubject());
    }

    public boolean isValid(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.debug("JWT validation failed: {}", e.getMessage());
            return false;
        }
    }

    public long accessTokenExpiryMs() {
        return props.getJwt().getAccessTokenExpiryMs();
    }
}
