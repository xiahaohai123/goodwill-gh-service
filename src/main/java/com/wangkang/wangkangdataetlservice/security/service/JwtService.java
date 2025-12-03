package com.wangkang.wangkangdataetlservice.security.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
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

    public String generateToken(String username, Collection<? extends GrantedAuthority> grantedAuthorities) {
        List<String> authorityNameList = grantedAuthorities.stream().map(GrantedAuthority::getAuthority).toList();
        return JWT.create()
                .withSubject(username)
                .withClaim("permissions", authorityNameList)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .sign(Algorithm.HMAC256(secretKey));
    }

    public String validateToken(String token) throws JWTVerificationException {
        return JWT.require(Algorithm.HMAC256(secretKey))
                .build()
                .verify(token)
                .getSubject();
    }
}
