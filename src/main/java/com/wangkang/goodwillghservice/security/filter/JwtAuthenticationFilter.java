package com.wangkang.goodwillghservice.security.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.wangkang.goodwillghservice.security.entity.CustomAuthenticationToken;
import com.wangkang.goodwillghservice.security.service.CustomUserDetailsService;
import com.wangkang.goodwillghservice.security.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final Log log = LogFactory.getLog(JwtAuthenticationFilter.class);

    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService,
                                   CustomUserDetailsService customUserDetailsService) {
        this.jwtService = jwtService;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        log.info("Filtering: " + path);

        String token = extractToken(request);
        if (token == null) {
            log.info("Missing token when request: " + path);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token is missing");
            return;
        }
        try {
            CustomAuthenticationToken customAuthenticationToken = jwtService.validateToken(token);
            Set<GrantedAuthority> grantedAuthorities = customUserDetailsService.loadByCustomAuthenticationToken(
                    customAuthenticationToken);
            Authentication auth = new CustomAuthenticationToken(grantedAuthorities,
                    customAuthenticationToken.getAreaCode(), customAuthenticationToken.getPhoneNumber());
            SecurityContextHolder.getContext().setAuthentication(auth);
            filterChain.doFilter(request, response);
        } catch (JWTVerificationException e) {
            log.warn(e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"error\":\"Invalid token\"}");
            response.getWriter().flush();
        }
    }

    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer")) {
            return header.substring(7);
        }
        // 再从 Cookie 拿
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        // 如果是登录接口，跳过本过滤器
        if ("/api/download".equals(path)) {
            return true;
        } else return "/api/auth/login".equals(path);
    }
}
