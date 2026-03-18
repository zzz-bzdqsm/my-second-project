package com.example.appointment.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

/**
 * JWT 生成与校验
 *
 * 你面试时可以这样讲：
 * - 登录成功后签发 token（包含 userId）
 * - 前端把 token 放在 Authorization: Bearer <token>
 * - 后端每次请求校验 token，解析出 userId
 *
 * 这里使用 HS256（对称加密）：服务端用同一个 secret 来签名与验签。
 */
@Service
public class JwtService {

    private final SecretKey key;
    private final long expireSeconds;

    public JwtService(@Value("${app.jwt.secret}") String secret,
                      @Value("${app.jwt.expire-seconds:7200}") long expireSeconds) {
        // HS256 需要足够长的 key，示例里用配置的字符串生成 key
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expireSeconds = expireSeconds;
    }

    public String createToken(Long userId) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(expireSeconds);
        return Jwts.builder()
                .subject(String.valueOf(userId))
                .issuedAt(Date.from(now))
                .expiration(Date.from(exp))
                .signWith(key)
                .compact();
    }

    public Long parseUserId(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        String sub = claims.getSubject();
        if (sub == null || sub.isBlank()) return null;
        return Long.parseLong(sub);
    }
}

