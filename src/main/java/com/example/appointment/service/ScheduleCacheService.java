package com.example.appointment.service;

import com.example.appointment.model.DoctorSchedule;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 排班查询缓存（Redis）
 *
 * 面试讲法：
 * - 排班是“读多写少”，非常适合缓存
 * - 这里缓存 key 按 doctorId + date 切分，TTL 短一点保证一致性
 * - 当预约创建/取消后，会删除对应 key，让下一次查询回源数据库
 */
@Service
public class ScheduleCacheService {
    private static final DateTimeFormatter KEY_DATE = DateTimeFormatter.BASIC_ISO_DATE; // yyyyMMdd
    private static final Duration TTL = Duration.ofSeconds(60); // 演示版：60 秒

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    public ScheduleCacheService(StringRedisTemplate redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    public String cacheKey(Long doctorId, LocalDate date) {
        return "schedule:list:doctor:" + doctorId + ":date:" + date.format(KEY_DATE);
    }

    public List<DoctorSchedule> get(Long doctorId, LocalDate date) {
        String key = cacheKey(doctorId, date);
        String json = redisTemplate.opsForValue().get(key);
        if (json == null || json.isBlank()) return null;
        try {
            return objectMapper.readValue(json, new TypeReference<List<DoctorSchedule>>() {});
        } catch (Exception e) {
            // 解析失败就当没缓存，让它回源 DB
            return null;
        }
    }

    public void set(Long doctorId, LocalDate date, List<DoctorSchedule> schedules) {
        String key = cacheKey(doctorId, date);
        try {
            String json = objectMapper.writeValueAsString(schedules);
            redisTemplate.opsForValue().set(key, json, TTL);
        } catch (Exception ignored) {
            // 写缓存失败不影响主流程
        }
    }

    public void evict(Long doctorId, LocalDate date) {
        redisTemplate.delete(cacheKey(doctorId, date));
    }
}

