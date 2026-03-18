package com.example.appointment.controller;

import com.example.appointment.auth.AuthContext;
import com.example.appointment.auth.RequireAuth;
import com.example.appointment.dto.ApiResponse;
import com.example.appointment.dto.LoginRequest;
import com.example.appointment.dto.LoginResponse;
import com.example.appointment.dto.RegisterRequest;
import com.example.appointment.mapper.UserMapper;
import com.example.appointment.model.User;
import com.example.appointment.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 鉴权相关接口：注册、登录、获取当前用户
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final UserMapper userMapper;

    public AuthController(AuthService authService, UserMapper userMapper) {
        this.authService = authService;
        this.userMapper = userMapper;
    }

    @PostMapping("/register")
    public ApiResponse<Void> register(@Valid @RequestBody RegisterRequest req) {
        authService.register(req);
        return ApiResponse.ok(null);
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest req) {
        return ApiResponse.ok(authService.login(req));
    }

    /**
     * 获取当前登录用户信息（需要 Authorization Bearer token）
     */
    @RequireAuth
    @GetMapping("/me")
    public ApiResponse<Map<String, Object>> me() {
        Long userId = AuthContext.getUserId();
        User user = userMapper.findById(userId);
        Map<String, Object> data = new HashMap<>();
        data.put("id", user.getId());
        data.put("username", user.getUsername());
        data.put("phone", user.getPhone());
        return ApiResponse.ok(data);
    }
}

