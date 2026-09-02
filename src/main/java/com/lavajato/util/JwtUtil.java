package com.lavajato.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.MacAlgorithm;

import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    private SecretKey secretKey = new SecretKeySpec("chaveSegura132chaveSegura132chaveSegura132".getBytes(), "HmacSHA256");
    private long expirationMillis = 3600000;
    private static final MacAlgorithm alg = Jwts.SIG.HS256;

    public JwtUtil(@Value("${jwt.secret}") String secret,
                   @Value("${jwt.expiration}") long expirationMillis) {
        this.secretKey = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
        this.expirationMillis = expirationMillis;
    }

    // Gerar token
    public String generateToken(String email) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMillis))
                .signWith(secretKey, alg)
                .compact();
    }

    // Extrair usuário (subject)
    public String extractUsername(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    // Validar token
    public boolean validateToken(String token, String email) {
        return email.equals(extractUsername(token)) && !isTokenExpired(token);
    }

    // Verificar expiração
    private boolean isTokenExpired(String token) {
        Date expiration = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();
        return expiration.before(new Date());
    }
}
