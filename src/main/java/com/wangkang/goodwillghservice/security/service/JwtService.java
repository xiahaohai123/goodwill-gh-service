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

    private static final long EXPIRATION_MS = 24L * 3600L * 1000L;

    @Value("${JWT_SECRET:key}")
    private String secretKey;

    /** 用于系统间调用 API 的 KEY */
    @Value("${goodwill-tool-station.secret-key}")
    private String innerSystemKey;

    public String generateToken(String areaCode,
                                String phoneNumber,
                                Collection<String> roles,
                                Collection<? extends GrantedAuthority> grantedAuthorities) {
        List<String> authorityNameList = grantedAuthorities.stream().map(GrantedAuthority::getAuthority).toList();
        List<String> roleList = roles.stream().toList();
        return JWT.create()
                .withClaim("areaCode", areaCode)
                .withClaim("phoneNumber", phoneNumber)
                .withClaim("roles", roleList)
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

    public String generateToken4GWToolStation(Collection<? extends GrantedAuthority> grantedAuthorities) {
        List<String> authorityNameList = grantedAuthorities.stream().map(GrantedAuthority::getAuthority).toList();
        long currentTimeMillis = System.currentTimeMillis();
        return JWT.create()
                .withIssuer("goodwill-gh-service")
                .withSubject("goodwill-gh-service")
                .withAudience("goodwill-tool-station-service")
                .withIssuedAt(new Date(currentTimeMillis))
                .withClaim("permissions", authorityNameList)
                .withExpiresAt(new Date(currentTimeMillis + 5 * 60 * 1000))
                .sign(Algorithm.HMAC256(innerSystemKey));
    }
}
