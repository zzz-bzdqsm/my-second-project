package com.example.appointment.dto;

/**
 * 统一返回格式（非常适合校招面试讲“规范化接口”）
 *
 * 前端收到的 JSON 结构统一为：
 * {
 *   "code": 0,
 *   "message": "ok",
 *   "data": {...}
 * }
 *
 * 好处：
 * - 前端处理简单（先看 code 再看 data）
 * - 后端错误也能统一格式返回（配合全局异常处理）
 */
public class ApiResponse<T> {
    private int code;
    private String message;
    private T data;

    public ApiResponse() {}

    public ApiResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(0, "ok", data);
    }

    public static <T> ApiResponse<T> error(int code, String message) {
        return new ApiResponse<>(code, message, null);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

