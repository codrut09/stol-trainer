package com.stoltrainer.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.logging.Logger;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret:}")
    private String jwtSecret;

    @Value("${jwt.expiration:86400000}")
    private long jwtExpiration;

    private static final Logger logger = Logger.getLogger(JwtTokenProvider.class.getName());
    private SecretKey signingKey;

    private SecretKey getSigningKey() {
        if (signingKey != null) {
            return signingKey;
        }

        try {
            // Dacă JWT_SECRET e gol sau nu e setat, generează o cheie sigură
            if (jwtSecret == null || jwtSecret.isBlank()) {
                logger.warning("JWT_SECRET not set, generating a secure key dynamically");
                signingKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
            } else {
                // Încearcă să decodezi din Base64
                byte[] keyBytes;
                try {
                    keyBytes = Base64.getDecoder().decode(jwtSecret);
                } catch (IllegalArgumentException e) {
                    // Dacă nu e Base64, convertește string-ul în bytes
                    keyBytes = jwtSecret.getBytes();
                }
                signingKey = Keys.hmacShaKeyFor(keyBytes);
            }
        } catch (Exception e) {
            logger.severe("Failed to initialize JWT signing key: " + e.getMessage());
            // Fallback: generează o cheie sigură
            signingKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        }

        return signingKey;
    }

    public String generateToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);

        return Jwts.builder()
                .subject(username)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return claims.getSubject();
        } catch (Exception e) {
            logger.warning("Failed to get username from token: " + e.getMessage());
            return null;
        }
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            logger.warning("JWT validation failed: " + e.getMessage());
            return false;
        }
    }

    public long getExpirationTime() {
        return jwtExpiration;
    }
}
