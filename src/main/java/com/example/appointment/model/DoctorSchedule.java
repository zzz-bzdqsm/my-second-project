package com.example.appointment.model;

import java.time.Instant;
import java.time.LocalDate;

/**
 * 医生排班/号源（doctor_schedule 表）
 *
 * timeSlot：
 * - AM：上午
 * - PM：下午
 */
public class DoctorSchedule {
    private Long id;
    private Long doctorId;
    private LocalDate scheduleDate;
    private String timeSlot;
    private Integer totalQuota;
    private Integer availableQuota;
    private Instant createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public LocalDate getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(LocalDate scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public Integer getTotalQuota() {
        return totalQuota;
    }

    public void setTotalQuota(Integer totalQuota) {
        this.totalQuota = totalQuota;
    }

    public Integer getAvailableQuota() {
        return availableQuota;
    }

    public void setAvailableQuota(Integer availableQuota) {
        this.availableQuota = availableQuota;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}

