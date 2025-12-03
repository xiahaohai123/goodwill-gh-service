package com.wangkang.wangkangdataetlservice.audit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Aspect
@Order(1)
@Component
public class SystemAuthenticationAspect {

    private static final Log log = LogFactory.getLog(SystemAuthenticationAspect.class);

    @Around("@annotation(com.wangkang.wangkangdataetlservice.audit.entity.SystemAuthenticated)")
    public Object injectSystemAuthentication(ProceedingJoinPoint joinPoint) throws Throwable {
        var context = SecurityContextHolder.getContext();
        var previousAuth = context.getAuthentication();

        try {
            log.debug("Injecting 'System' authentication for: " + joinPoint.getSignature());
            UsernamePasswordAuthenticationToken systemAuth =
                    new UsernamePasswordAuthenticationToken("System", null, Collections.emptyList());

            context.setAuthentication(systemAuth);
            return joinPoint.proceed();
        } finally {
            // 恢复原认证信息，避免污染线程
            context.setAuthentication(previousAuth);
        }
    }
}
