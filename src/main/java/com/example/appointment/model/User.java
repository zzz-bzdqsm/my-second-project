package com.example.appointment.model;

import java.time.Instant;

/**
 * 用户实体（对应数据库 users 表）
 *
 * 注意：
 * - passwordHash 存的是 BCrypt 的密文，不存明文密码
 * - 校招演示中：我们用 username+password 登录
 */
public class User {
    private Long id;
    private String username;
    private String passwordHash;
    private String phone;
    private Instant createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}

