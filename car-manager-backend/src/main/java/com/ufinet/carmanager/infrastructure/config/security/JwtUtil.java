package com.ufinet.carmanager.infrastructure.config.security;

import com.ufinet.carmanager.domain.auth.ports.out.JwtProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class JwtUtil implements JwtProvider {
    public static final String CLAIM_USER_ID      = "sub";
    private static final long TOKEN_AUTH_MINUTES = 60;

    private final SecretKey key;

    public JwtUtil(@Value("${security.jwt.secret}") String secret) {
        if (secret == null || secret.length() < 32) {
            throw new IllegalArgumentException("Invalid JWT secret key length" + (secret == null ? "null" : secret.length()));
        }
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String generateAuthToken(Long userId) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(String.valueOf(userId))
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(TOKEN_AUTH_MINUTES, ChronoUnit.MINUTES)))
                .signWith(key)
                .compact();
    }

    public TokenValidationResult validate(String token) {
        if (token == null || token.isBlank())
            return TokenValidationResult.MISSING;

        try {
            extractAllClaims(token);
            return TokenValidationResult.VALID;
        } catch (ExpiredJwtException e) {
            return TokenValidationResult.EXPIRED;
        //} catch (SignatureException e) {
        //    return TokenValidationResult.INVALID_SIGNATURE;
        } catch (MalformedJwtException e) {
            return TokenValidationResult.MALFORMED;
        } catch (JwtException | IllegalArgumentException e) {
            return TokenValidationResult.INVALID;
        }
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Long extractUserId(String token) {
        return Long.valueOf(extractAllClaims(token).getSubject());
    }

    public enum TokenValidationResult {
        VALID,
        EXPIRED,
        INVALID_SIGNATURE,
        MALFORMED,
        MISSING,
        INVALID;

        public String toMessage() {
            return switch (this) {
                case VALID             -> "Token valid";
                case EXPIRED           -> "Token expired";
                case INVALID_SIGNATURE -> "Token signature invalid";
                case MALFORMED         -> "Token malformed";
                case MISSING           -> "Token empty";
                case INVALID           -> "Token invalid";
            };
        }
    }
}