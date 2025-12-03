package com.wangkang.wangkangdataetlservice.audit.entity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** 审计日志中用来标记字段名的注解 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuditParam {
    String name(); // 用于日志中显示的字段名
}
