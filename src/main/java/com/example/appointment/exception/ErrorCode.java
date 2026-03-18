package com.example.appointment.exception;

/**
 * 错误码枚举（校招面试：体现“可维护性/可定位性”）
 *
 * 约定：
 * - 0 表示成功（见 ApiResponse.ok）
 * - 其它均为业务/系统错误
 */
public enum ErrorCode {
    BAD_REQUEST(40000, "参数错误"),
    UNAUTHORIZED(40100, "未登录或登录已过期"),
    FORBIDDEN(40300, "无权限"),
    NOT_FOUND(40400, "资源不存在"),
    CONFLICT(40900, "冲突（例如重复预约/号源不足）"),
    INTERNAL_ERROR(50000, "服务内部错误");

    private final int code;
    private final String defaultMessage;

    ErrorCode(int code, String defaultMessage) {
        this.code = code;
        this.defaultMessage = defaultMessage;
    }

    public int getCode() {
        return code;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }
}

