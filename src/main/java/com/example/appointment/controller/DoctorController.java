package com.example.appointment.controller;

import com.example.appointment.dto.ApiResponse;
import com.example.appointment.model.Doctor;
import com.example.appointment.service.DoctorService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 医生相关接口（公开接口：不需要登录）
 */
@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping
    public ApiResponse<List<Doctor>> list(@RequestParam(required = false) String dept,
                                         @RequestParam(required = false) String q,
                                         @RequestParam(defaultValue = "1") int page,
                                         @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.ok(doctorService.list(dept, q, page, size));
    }

    @GetMapping("/{id}")
    public ApiResponse<Doctor> get(@PathVariable Long id) {
        return ApiResponse.ok(doctorService.get(id));
    }

    @GetMapping("/depts")
    public ApiResponse<Map<String, Object>> depts() {
        Map<String, Object> data = new HashMap<>();
        data.put("items", doctorService.depts());
        return ApiResponse.ok(data);
    }
}

