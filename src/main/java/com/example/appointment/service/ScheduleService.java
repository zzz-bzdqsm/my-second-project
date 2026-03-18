package com.example.appointment.service;

import com.example.appointment.mapper.DoctorScheduleMapper;
import com.example.appointment.model.DoctorSchedule;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * 排班查询业务（带 Redis 缓存）
 */
@Service
public class ScheduleService {

    private final DoctorScheduleMapper scheduleMapper;
    private final ScheduleCacheService cacheService;

    public ScheduleService(DoctorScheduleMapper scheduleMapper, ScheduleCacheService cacheService) {
        this.scheduleMapper = scheduleMapper;
        this.cacheService = cacheService;
    }

    public List<DoctorSchedule> list(Long doctorId, LocalDate date) {
        // 先读缓存
        List<DoctorSchedule> cached = cacheService.get(doctorId, date);
        if (cached != null) {
            return cached;
        }

        // 回源 DB
        List<DoctorSchedule> schedules = scheduleMapper.listByDoctorAndDate(doctorId, date);
        // 写缓存
        cacheService.set(doctorId, date, schedules);
        return schedules;
    }

    public void evictCache(Long doctorId, LocalDate date) {
        cacheService.evict(doctorId, date);
    }
}

