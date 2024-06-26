package com.solo.toygateway.util;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JWTUtil {

    private final SecretKey secretKey;

    public JWTUtil(@Value("${jwt.secret}")
                   String secret
    ) {

        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    //토큰 검증을 위해 페이로드의 값을 복호화
    private Claims decodeToken(String token) {

        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    //category - access, refresh
    public String getCategory(String token) {

        return decodeToken(token).get("category", String.class);
    }

    public Boolean isExpired(String token) {

        return decodeToken(token).getExpiration().before(new Date());
    }
}
