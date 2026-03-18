package com.example.appointment.dto;

/**
 * 登录响应：返回 JWT token
 */
public class LoginResponse {
    private String token;

    public LoginResponse() {}

    public LoginResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

