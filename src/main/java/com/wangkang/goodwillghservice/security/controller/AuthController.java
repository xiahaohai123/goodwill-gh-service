package com.wangkang.goodwillghservice.security.controller;

import com.wangkang.goodwillghservice.audit.entity.TaskStatus;
import com.wangkang.goodwillghservice.dao.goodwillghservice.audit.model.LoginLog;
import com.wangkang.goodwillghservice.dao.goodwillghservice.audit.repository.LoginLogRepository;
import com.wangkang.goodwillghservice.dao.goodwillghservice.security.model.User;
import com.wangkang.goodwillghservice.dao.goodwillghservice.security.repository.UserRepository;
import com.wangkang.goodwillghservice.security.entity.LoginRequest;
import com.wangkang.goodwillghservice.security.service.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Log log = LogFactory.getLog(AuthController.class);
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final LoginLogRepository loginLogRepository;
    @Value("${app.cookie-secure}")
    private boolean cookieSecure;

    private static final String DISPLAY_NAME = "displayName";
    private static final String TOKEN = "token";

    @Autowired
    public AuthController(PasswordEncoder passwordEncoder,
                          UserRepository userRepository,
                          JwtService jwtService,
                          UserDetailsService userDetailsService, LoginLogRepository loginLogRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.loginLogRepository = loginLogRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequest loginRequest,
                                        HttpServletRequest request,
                                        HttpServletResponse response) {
        User user = userRepository.findByUsernameIgnoreCase(loginRequest.getUsername());

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid username or password"));
        }

        LoginLog loginLog = new LoginLog();
        loginLog.setUsername(user.getUsername());
        loginLog.setDisplayName(user.getDisplayName());
        loginLog.setUserId(user.getId());
        loginLog.setIpAddress(getClientIp(request));
        loginLog.setUserAgent(getUserAgent(request));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            loginLog.setStatus(TaskStatus.FAILED.name());
            loginLog.setFailureReason("Incorrect account password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid username or password"));
        }
        log.info("Login: " + user.getUsername());
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        String token = jwtService.generateToken(loginRequest.getUsername(), authorities);

        String displayName = URLEncoder.encode(user.getDisplayName(), StandardCharsets.UTF_8);
        List<Cookie> cookies = List.of(
                new Cookie(TOKEN, token),
                new Cookie(DISPLAY_NAME, displayName));
        cookies.forEach(cookie -> {
            cookie.setHttpOnly(true);
            cookie.setSecure(cookieSecure); // 生产环境必须启用
            cookie.setPath("/"); // 作用于整个域
            cookie.setMaxAge(24 * 60 * 60); // 1天过期
        });
        cookies.forEach(response::addCookie);

        loginLog.setStatus(TaskStatus.SUCCESS.name());
        loginLogRepository.save(loginLog);
        return ResponseEntity.ok().body(Map.of(TOKEN, token,
                DISPLAY_NAME, user.getDisplayName()));
    }

    public String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            return ip.split(",")[0].trim(); // 取第一个 IP
        }
        ip = request.getHeader("X-Real-IP");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }
        return request.getRemoteAddr();
    }

    public String getUserAgent(HttpServletRequest request) {
        return request.getHeader("User-Agent");
    }

    @PostMapping("/logout")
    public ResponseEntity<Object> logout(HttpServletResponse response) {
        log.info("Logout");
        List<Cookie> cookies = List.of(
                new Cookie(TOKEN, null),
                new Cookie(DISPLAY_NAME, null));
        cookies.forEach(cookie -> {
            cookie.setHttpOnly(true);
            cookie.setSecure(cookieSecure); // 生产环境必须启用
            cookie.setPath("/"); // 作用于整个域
            cookie.setMaxAge(0);
        });
        cookies.forEach(response::addCookie);
        return ResponseEntity.ok().build();
    }

}
