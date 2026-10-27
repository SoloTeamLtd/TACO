package com.example.springinaction.tacoapp.auth.service;

import org.springframework.stereotype.Component;

// оставляем на rest
@Component
public class JWTUtil {
/*
    @Value("${jwt.secret}")
    private String secret;

    private static final String ISSUER = "tacoapp";
    private static final String SUBJECT = "User Details";

    public String generateToken(String phone, String role) {
        return JWT.create()
                .withSubject(SUBJECT)
                .withClaim("phone", phone) // Называем поле прозрачно — phone
                .withClaim("role", role)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 60 * 60 * 1000)) // 1 час
                .withIssuer(ISSUER)
                .sign(Algorithm.HMAC256(secret));
    }

    public Map<String, String> validateTokenAndRetrieveClaims(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                .withSubject(SUBJECT)
                .withIssuer(ISSUER) // Исправлена опечатка!
                .build();

        DecodedJWT jwt = verifier.verify(token);

        Map<String, String> claims = new HashMap<>();
        claims.put("phone", jwt.getClaim("phone").asString());
        claims.put("role", jwt.getClaim("role").asString());
        return claims;
    }*/
}
