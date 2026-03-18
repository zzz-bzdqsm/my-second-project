package com.example.appointment.exception;

/**
 * 业务异常（由我们主动抛出，用于返回“可控的错误码/错误信息”）
 *
 * 例如：
 * - 用户名已存在
 * - 密码错误
 * - 号源不足
 */
public class ApiException extends RuntimeException {
    private final ErrorCode errorCode;

    public ApiException(ErrorCode errorCode) {
        super(errorCode.getDefaultMessage());
        this.errorCode = errorCode;
    }

    public ApiException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}

