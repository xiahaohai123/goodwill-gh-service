package com.wangkang.goodwillghservice.security.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.wangkang.goodwillghservice.security.entity.CustomAuthenticationToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Service
public class JwtService {

    private static final long EXPIRATION_MS = 12L * 3600L * 1000L;

    @Value("${JWT_SECRET:key}")
    private String secretKey;

    public String generateToken(String areaCode,
                                String phoneNumber,
                                Collection<? extends GrantedAuthority> grantedAuthorities) {
        List<String> authorityNameList = grantedAuthorities.stream().map(GrantedAuthority::getAuthority).toList();
        return JWT.create()
                .withClaim("areaCode", areaCode)
                .withClaim("phoneNumber", phoneNumber)
                .withClaim("permissions", authorityNameList)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .sign(Algorithm.HMAC256(secretKey));
    }

    public CustomAuthenticationToken validateToken(String token) throws JWTVerificationException {
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(secretKey))
                .build()
                .verify(token);
        String areaCode = decodedJWT.getClaim("areaCode").asString();
        String phoneNumber = decodedJWT.getClaim("phoneNumber").asString();
        return new CustomAuthenticationToken(areaCode, phoneNumber);
    }
}
