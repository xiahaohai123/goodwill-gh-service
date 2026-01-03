package com.wangkang.goodwillghservice.security.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.wangkang.goodwillghservice.security.SecurityConstants;
import com.wangkang.goodwillghservice.security.entity.CustomAuthenticationToken;
import com.wangkang.goodwillghservice.security.entity.CustomUserDetails;
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
import org.springframework.http.server.PathContainer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final Log log = LogFactory.getLog(JwtAuthenticationFilter.class);

    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;
    private final List<PathPattern> whiteListPatterns;

    public JwtAuthenticationFilter(JwtService jwtService,
                                   CustomUserDetailsService customUserDetailsService) {
        this.jwtService = jwtService;
        this.customUserDetailsService = customUserDetailsService;
        PathPatternParser parser = new PathPatternParser();
        this.whiteListPatterns = SecurityConstants.WHITE_LIST.stream()
                .map(parser::parse)
                .toList();
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
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"error\":\"Token is missing\"}");
            response.getWriter().flush(); // 必须 flush 才能确保立即返回
            return;
        }
        try {
            CustomAuthenticationToken customAuthenticationToken = jwtService.validateToken(token);
            CustomUserDetails customUserDetails = customUserDetailsService.loadByCustomAuthenticationToken(
                    customAuthenticationToken);
            Set<GrantedAuthority> grantedAuthorities = customUserDetails.getGrantedAuthorities();
            Authentication auth = new CustomAuthenticationToken(grantedAuthorities, customUserDetails.getUserId(),
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
        // PathPattern 匹配的是 PathContainer
        PathContainer path = PathContainer.parsePath(request.getRequestURI());
        return whiteListPatterns.stream().anyMatch(pattern -> pattern.matches(path));
    }
}
