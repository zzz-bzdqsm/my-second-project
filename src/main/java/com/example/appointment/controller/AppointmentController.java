package com.example.appointment.controller;

import com.example.appointment.auth.AuthContext;
import com.example.appointment.auth.RequireAuth;
import com.example.appointment.dto.ApiResponse;
import com.example.appointment.dto.CreateAppointmentRequest;
import com.example.appointment.model.Appointment;
import com.example.appointment.service.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 预约接口（需要登录）
 */
@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    /**
     * 创建预约
     */
    @RequireAuth
    @PostMapping
    public ApiResponse<Map<String, Object>> create(@Valid @RequestBody CreateAppointmentRequest req) {
        Long userId = AuthContext.getUserId();
        Appointment appt = appointmentService.create(userId, req);
        Map<String, Object> data = new HashMap<>();
        data.put("appointmentId", appt.getId());
        return ApiResponse.ok(data);
    }

    /**
     * 我的预约列表
     */
    @RequireAuth
    @GetMapping("/my")
    public ApiResponse<List<Appointment>> my(@RequestParam(defaultValue = "50") int limit) {
        Long userId = AuthContext.getUserId();
        return ApiResponse.ok(appointmentService.my(userId, limit));
    }

    /**
     * 取消预约
     */
    @RequireAuth
    @PostMapping("/{id}/cancel")
    public ApiResponse<Void> cancel(@PathVariable Long id) {
        Long userId = AuthContext.getUserId();
        appointmentService.cancel(userId, id);
        return ApiResponse.ok(null);
    }
}

