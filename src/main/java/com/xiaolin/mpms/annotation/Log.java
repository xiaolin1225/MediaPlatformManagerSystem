/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.annotation;


import com.xiaolin.mpms.enums.LogType;
import com.xiaolin.mpms.enums.OperationType;

import java.lang.annotation.*;

/**
 * 日志注解
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {
    /**
     * 模块
     */
    public String title() default "";

    /**
     * 操作类型
     */
    public OperationType operationType() default OperationType.OTHER;

    /**
     * 操作成功消息
     */
    public String successMessage() default "操作成功";

    /**
     * 操作失败消息
     */
    public String errorMessage() default "操作失败";

    /**
     * 日志类型
     */
    public LogType logType() default LogType.OTHER;

    /**
     * 是否保存请求的参数
     */
    public boolean isSaveRequestData() default true;

    /**
     * 是否保存响应的参数
     */
    public boolean isSaveResponseData() default false;

    /**
     * 排除指定的请求参数
     */
    public String[] excludeParamNames() default {};
}
