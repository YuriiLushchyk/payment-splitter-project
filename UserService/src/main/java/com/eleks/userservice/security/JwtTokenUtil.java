package com.eleks.userservice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenUtil implements Serializable {
    public static final long JWT_VALIDITY_TIME_MILLIS = 60 * 60 * 1000;

    private String secret;

    public JwtTokenUtil(@Value("${jwt.secret}") String secret) {
        this.secret = secret;
    }

    public Long getUserIdFromToken(String token) {
        return Long.valueOf(getClaimFromToken(token, Claims::getSubject));
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        return claimsResolver.apply(claims);
    }

    public String generateToken(Long userId) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userId.toString(), JWT_VALIDITY_TIME_MILLIS);
    }

    public String doGenerateToken(Map<String, Object> claims, String subject, long validityTimeMillis) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + validityTimeMillis))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }
}
