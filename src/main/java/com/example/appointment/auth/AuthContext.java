package com.example.appointment.auth;

/**
 * 一个非常轻量的“登录上下文”（ThreadLocal）
 *
 * 为什么需要它？
 * - JWT 校验通过后，我们会得到 userId
 * - 业务代码（Service/Controller）经常需要拿到当前登录用户的 userId
 *
 * 生产项目常用 Spring Security 的 SecurityContext；这里为了校招演示，用最简单能理解的方式。
 */
public class AuthContext {
    private static final ThreadLocal<Long> USER_ID_HOLDER = new ThreadLocal<>();

    public static void setUserId(Long userId) {
        USER_ID_HOLDER.set(userId);
    }

    public static Long getUserId() {
        return USER_ID_HOLDER.get();
    }

    public static void clear() {
        USER_ID_HOLDER.remove();
    }
}

