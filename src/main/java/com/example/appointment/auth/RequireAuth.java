package com.example.appointment.auth;

import java.lang.annotation.*;

/**
 * 标记某个接口需要登录（JWT）
 *
 * 用法：
 * - 在 Controller 方法上加 @RequireAuth
 * - 拦截器会校验 Authorization 里的 token，并把 userId 放到 AuthContext
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequireAuth {
}

