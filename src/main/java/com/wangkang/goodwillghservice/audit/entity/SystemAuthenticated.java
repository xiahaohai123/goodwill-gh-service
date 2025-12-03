package com.wangkang.goodwillghservice.audit.entity;

import java.lang.annotation.*;

/** 标记一些守护线程为系统身份来执行的 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SystemAuthenticated {
}
