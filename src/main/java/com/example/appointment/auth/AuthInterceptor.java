package com.example.appointment.auth;

import com.example.appointment.exception.ApiException;
import com.example.appointment.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 登录拦截器：
 * - 仅当接口标注了 @RequireAuth 时才会校验 JWT
 * - 校验通过把 userId 放到 AuthContext（ThreadLocal）
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtService jwtService;

    public AuthInterceptor(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!(handler instanceof HandlerMethod hm)) {
            return true;
        }

        boolean needAuth =
                hm.hasMethodAnnotation(RequireAuth.class) ||
                hm.getBeanType().isAnnotationPresent(RequireAuth.class);

        if (!needAuth) {
            return true;
        }

        String auth = request.getHeader("Authorization");
        if (auth == null || auth.isBlank() || !auth.startsWith("Bearer ")) {
            throw new ApiException(ErrorCode.UNAUTHORIZED, "缺少 Authorization: Bearer <token>");
        }

        String token = auth.substring("Bearer ".length()).trim();
        try {
            Long userId = jwtService.parseUserId(token);
            if (userId == null) {
                throw new ApiException(ErrorCode.UNAUTHORIZED, "token 无效");
            }
            AuthContext.setUserId(userId);
            return true;
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException(ErrorCode.UNAUTHORIZED, "token 无效或已过期");
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        AuthContext.clear();
    }
}

