package com.example.springinaction.tacoapp.security;

import com.auth0.jwt.*;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JWTUtil {

    @Value("${jwt.secret}")
    private String secret;

    public String generateToken(String username, String role) {
        return JWT.create()
                .withSubject("User Details")
                .withClaim("username", username)
                .withClaim("role", role)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 60 * 60 * 1000)) // 1 час
                .withIssuer("tacoapp")
                .sign(Algorithm.HMAC256(secret));
    }

    // Проверяем подпись токена математически (без запросов в БД)
    public Map<String, String> validateTokenAndRetrieveClaims(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                .withSubject("User Details")
                .withIssuer("tcoapp")
                .build();
        DecodedJWT jwt = verifier.verify(token);
        Map<String, String> claims = new HashMap<>();
        claims.put("username", jwt.getClaim("username").asString());
        claims.put("role", jwt.getClaim("role").asString());
        return claims;
    }
}
