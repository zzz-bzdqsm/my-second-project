package com.example.appointment.controller;

import com.example.appointment.dto.ApiResponse;
import com.example.appointment.model.DoctorSchedule;
import com.example.appointment.service.ScheduleService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

/**
 * 排班接口（公开接口：不需要登录）
 */
@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    /**
     * 查询排班
     * 示例：GET /api/schedules?doctorId=1&date=2026-03-17
     */
    @GetMapping
    public ApiResponse<List<DoctorSchedule>> list(@RequestParam Long doctorId,
                                                  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ApiResponse.ok(scheduleService.list(doctorId, date));
    }
}

