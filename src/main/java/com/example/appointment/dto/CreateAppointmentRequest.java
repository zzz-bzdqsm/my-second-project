package com.example.appointment.dto;

import jakarta.validation.constraints.NotNull;

/**
 * 创建预约请求
 */
public class CreateAppointmentRequest {
    @NotNull
    private Long scheduleId;

    public Long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }
}

