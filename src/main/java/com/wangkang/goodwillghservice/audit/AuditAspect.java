package com.wangkang.goodwillghservice.audit;

import com.wangkang.goodwillghservice.audit.entity.*;
import com.wangkang.goodwillghservice.dao.goodwillghservice.audit.model.OperationLog;
import com.wangkang.goodwillghservice.dao.goodwillghservice.audit.repository.OperationLogRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.time.*;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@Aspect
@Component
@Order(10)
public class AuditAspect {

    private final ThreadLocal<OffsetDateTime> startTime = new ThreadLocal<>();
    private final OperationLogRepository operationLogRepository;

    @Autowired
    public AuditAspect(OperationLogRepository operationLogRepository) {
        this.operationLogRepository = operationLogRepository;
    }

    @Pointcut("@annotation(auditable)")
    public void auditPoint(Auditable auditable) {
    }

    @Before(value = "auditPoint(auditable)", argNames = "jp,auditable")
    public void before(JoinPoint jp, Auditable auditable) {
        startTime.set(OffsetDateTime.now());
    }

    @AfterReturning(pointcut = "auditPoint(auditable)", returning = "ret", argNames = "jp,auditable,ret")
    public void onSuccess(JoinPoint jp, Auditable auditable, Object ret) {
        Map<String, Object> details = extractJoinPoint2Details(jp);
        Object result = auditable.logResult() ? ret : null;
        saveOperationLog(auditable.actionName(), auditable.actionType(), TaskStatus.SUCCESS.name(), details, result,
                null);
    }

    @AfterThrowing(pointcut = "auditPoint(auditable)", throwing = "ex", argNames = "jp,auditable,ex")
    public void onFailure(JoinPoint jp, Auditable auditable, Throwable ex) {
        Map<String, Object> details = extractJoinPoint2Details(jp);
        saveOperationLog(auditable.actionName(), auditable.actionType(), TaskStatus.FAILED.name(), details, null,
                ex.getMessage());
    }

    private void saveOperationLog(String actionName,
                                  ActionType actionType,
                                  String status,
                                  Map<String, Object> details,
                                  Object result,
                                  String errorMsg) {
        OffsetDateTime executedAt = startTime.get();
        Duration duration = Duration.between(executedAt, OffsetDateTime.now());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String executor;
        if (authentication == null) {
            executor = "unknown";
        } else {
            executor = authentication.getName();
        }
        OperationLog operationLog = new OperationLog();
        operationLog.setOperationName(actionName);
        operationLog.setOperationType(actionType.value);
        operationLog.setStatus(status);
        operationLog.setDetail(details);
        operationLog.setErrorMsg(errorMsg);
        operationLog.setExecutor(executor);
        operationLog.setExecutedAt(executedAt);
        operationLog.setDurationMs(duration.toMillis());
        if (result != null) {
            operationLog.setResultData(result);
        }
        operationLogRepository.save(operationLog);
        startTime.remove();
    }

    private Map<String, Object> extractJoinPoint2Details(JoinPoint jp) {
        MethodSignature sig = (MethodSignature) jp.getSignature();
        Object[] args = jp.getArgs();
        String[] paramNames = sig.getParameterNames();
        Method method = sig.getMethod();
        // 一个参数允许有多个注解，所以是二维数组，第一维度是参数数量，第二维度是每个参数对应的注解
        Annotation[][] paramAnns = method.getParameterAnnotations();
        Map<String, Object> details = new LinkedHashMap<>();
        if (paramNames == null) {
            return details;
        }
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            if (arg == null) continue;
            String name = paramNames[i];
            // 检查是对应参数是否带需要的注解
            AuditParam ap = findAnnotation(paramAnns[i], AuditParam.class);
            String key = ap != null ? ap.name() : name;
            details.put(key, makeDetails(arg));
        }
        return details;
    }

    private Object makeDetails(Object arg) {
        if (isPrimitiveOrWrapper(arg.getClass()) || arg instanceof String) {
            return arg; // 基本类型无需处理
        }

        // 特定类型：直接调用 toString
        if (arg instanceof LocalDate ||
                arg instanceof LocalDateTime ||
                arg instanceof Instant ||
                arg instanceof UUID ||
                arg instanceof OffsetDateTime) {
            return arg.toString();
        }

        // 集合处理
        if (arg instanceof Collection<?>) {
            return ((Collection<?>) arg).stream()
                    .map(this::makeDetails)
                    .toList();
        }

        // 对象类型：构建新的 map，反射字段
        Map<String, Object> map = new LinkedHashMap<>();
        for (Field f : arg.getClass().getDeclaredFields()) {
            ReflectionUtils.makeAccessible(f); // 内部判断再 setAccessible
            // 跳过静态字段 // 跳过常量 // 跳过敏感字段
            if (Modifier.isStatic(f.getModifiers()) || Modifier.isFinal(f.getModifiers()) || f.isAnnotationPresent(
                    Sensitive.class)) continue;
            Object value = ReflectionUtils.getField(f, arg);
            if (value != null) {
                map.put(f.getName(), makeDetails(value)); // 递归
            }
        }
        return map;
    }

    /**
     * 检查是否是原始类型
     * @param cls 类型
     * @return 是否原始类型
     */
    private boolean isPrimitiveOrWrapper(Class<?> cls) {
        return cls.isPrimitive()
                || cls == String.class
                || Number.class.isAssignableFrom(cls)
                || cls == Boolean.class
                || cls == Character.class;
    }

    /**
     * 从注解数组中查找指定类型的注解
     */
    private <A extends Annotation> A findAnnotation(Annotation[] annotations, Class<A> clz) {
        for (Annotation ann : annotations) {
            if (clz.isInstance(ann)) {
                return clz.cast(ann);
            }
        }
        return null;
    }
}
