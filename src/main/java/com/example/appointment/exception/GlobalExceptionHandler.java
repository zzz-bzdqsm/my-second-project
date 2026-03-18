package com.example.appointment.exception;

import com.example.appointment.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理（把异常转换为统一 JSON 返回）
 *
 * 面试讲法：
 * - Controller 里不需要到处 try/catch
 * - 业务异常用 ApiException，统一返回明确 code
 * - 参数校验错误也统一输出，前端更好处理
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ApiResponse<Void> handleApiException(ApiException e) {
        return ApiResponse.error(e.getErrorCode().getCode(), e.getMessage());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class, IllegalArgumentException.class})
    public ApiResponse<Void> handleBadRequest(Exception e) {
        String msg = e.getMessage();
        return ApiResponse.error(ErrorCode.BAD_REQUEST.getCode(), msg != null ? msg : ErrorCode.BAD_REQUEST.getDefaultMessage());
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse<Void> handleUnknown(Exception e, HttpServletRequest request) {
        // 生产环境可以记录日志：URI、参数、堆栈等；这里为了演示返回统一错误
        return ApiResponse.error(ErrorCode.INTERNAL_ERROR.getCode(), ErrorCode.INTERNAL_ERROR.getDefaultMessage());
    }
}

