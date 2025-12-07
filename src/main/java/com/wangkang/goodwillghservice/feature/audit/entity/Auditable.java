package com.wangkang.goodwillghservice.feature.audit.entity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Auditable {
    ActionType actionType();

    String actionName();

    /** 是否记录返回值 */
    boolean logResult() default true;
}
