package com.example.appointment.service;

import com.example.appointment.auth.JwtService;
import com.example.appointment.dto.LoginRequest;
import com.example.appointment.dto.LoginResponse;
import com.example.appointment.dto.RegisterRequest;
import com.example.appointment.exception.ApiException;
import com.example.appointment.exception.ErrorCode;
import com.example.appointment.mapper.UserMapper;
import com.example.appointment.model.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 注册/登录 业务
 */
@Service
public class AuthService {

    private final UserMapper userMapper;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthService(UserMapper userMapper, JwtService jwtService) {
        this.userMapper = userMapper;
        this.jwtService = jwtService;
    }

    public void register(RegisterRequest req) {
        String username = req.getUsername().trim();
        if (username.length() < 3) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "用户名至少 3 个字符");
        }
        if (req.getPassword().length() < 6) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "密码至少 6 位");
        }

        User exists = userMapper.findByUsername(username);
        if (exists != null) {
            throw new ApiException(ErrorCode.CONFLICT, "用户名已存在");
        }

        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(passwordEncoder.encode(req.getPassword()));
        user.setPhone(req.getPhone());
        userMapper.insert(user);
    }

    public LoginResponse login(LoginRequest req) {
        User user = userMapper.findByUsername(req.getUsername().trim());
        if (user == null) {
            throw new ApiException(ErrorCode.UNAUTHORIZED, "用户名或密码错误");
        }
        boolean ok = passwordEncoder.matches(req.getPassword(), user.getPasswordHash());
        if (!ok) {
            throw new ApiException(ErrorCode.UNAUTHORIZED, "用户名或密码错误");
        }
        String token = jwtService.createToken(user.getId());
        return new LoginResponse(token);
    }
}

