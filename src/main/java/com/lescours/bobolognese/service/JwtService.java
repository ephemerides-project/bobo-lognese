package com.lescours.bobolognese.service;

import com.lescours.bobolognese.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.enterprise.context.ApplicationScoped;

import javax.crypto.SecretKey;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Optional;

@ApplicationScoped
public class JwtService implements Serializable {

    private static final String SECRET_KEY = "bobo-lognese-super-secret-key-for-jwt-authentication-minimum-256-bits";
    private static final long EXPIRATION_TIME_MS = 24 * 60 * 60 * 1000;
    public static final String JWT_COOKIE_NAME = "jwt";

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(User user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME_MS);

        return Jwts.builder()
                .subject(user.getUsername())
                .claim("userId", user.getId())
                .claim("role", user.getRole().name())
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }

    public Optional<Claims> validateToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return Optional.of(claims);
        } catch (JwtException | IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    public Optional<String> extractUsername(String token) {
        return validateToken(token).map(Claims::getSubject);
    }

    public boolean isTokenExpired(String token) {
        return validateToken(token)
                .map(claims -> claims.getExpiration().before(new Date()))
                .orElse(true);
    }
}
