package dev.sorokin.eventnotificator.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
public class JwtTokenManager {

    private final SecretKey key;

    public JwtTokenManager(@Value("${jwt.secret-key}") String key) {
        this.key = Keys.hmacShaKeyFor(key.getBytes());
    }

    public AuthenticatedUser parseUser(String jwt) {
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(jwt)
                .getPayload();

        Number userId = claims.get("userId", Number.class);
        if (userId == null) {
            throw new IllegalArgumentException("JWT does not contain userId claim");
        }

        String role = claims.get("role", String.class);
        return new AuthenticatedUser(userId.longValue(), claims.getSubject(), role == null ? "USER" : role);
    }
}
