package com.example.appointment.service;

import com.example.appointment.dto.CreateAppointmentRequest;
import com.example.appointment.exception.ApiException;
import com.example.appointment.exception.ErrorCode;
import com.example.appointment.mapper.AppointmentMapper;
import com.example.appointment.mapper.DoctorScheduleMapper;
import com.example.appointment.model.Appointment;
import com.example.appointment.model.DoctorSchedule;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 预约核心业务
 *
 * 你面试可以重点讲这段：
 * - 如何防止“超卖”（并发扣减号源）
 * - 为什么要加事务（扣减号源 + 插入预约必须一致）
 * - 取消预约如何回补号源
 */
@Service
public class AppointmentService {

    private final DoctorScheduleMapper scheduleMapper;
    private final AppointmentMapper appointmentMapper;
    private final ScheduleService scheduleService;

    public AppointmentService(DoctorScheduleMapper scheduleMapper,
                              AppointmentMapper appointmentMapper,
                              ScheduleService scheduleService) {
        this.scheduleMapper = scheduleMapper;
        this.appointmentMapper = appointmentMapper;
        this.scheduleService = scheduleService;
    }

    /**
     * 创建预约（防超卖：原子扣减 + 事务）
     */
    @Transactional
    public Appointment create(Long userId, CreateAppointmentRequest req) {
        Long scheduleId = req.getScheduleId();

        // 1) 防重复预约：同一个用户对同一个 schedule 不允许重复 BOOKED
        Appointment existing = appointmentMapper.findActiveByUserAndSchedule(userId, scheduleId);
        if (existing != null) {
            throw new ApiException(ErrorCode.CONFLICT, "你已经预约过该号源了");
        }

        // 2) 原子扣减号源（最关键：并发下不会扣成负数）
        int updated = scheduleMapper.decreaseQuotaIfAvailable(scheduleId);
        if (updated != 1) {
            throw new ApiException(ErrorCode.CONFLICT, "号源不足，请选择其他时间段");
        }

        // 3) 查询 schedule，拿到 doctorId（插入预约表时冗余一份）
        DoctorSchedule schedule = scheduleMapper.findById(scheduleId);
        if (schedule == null) {
            // 理论上不会发生：因为 scheduleId 不存在时 update 返回 0
            throw new ApiException(ErrorCode.NOT_FOUND, "排班不存在");
        }

        // 4) 插入预约记录
        Appointment appt = new Appointment();
        appt.setUserId(userId);
        appt.setDoctorId(schedule.getDoctorId());
        appt.setScheduleId(scheduleId);
        appt.setStatus("BOOKED");
        appointmentMapper.insert(appt);

        // 5) 缓存失效：预约会改变 availableQuota
        scheduleService.evictCache(schedule.getDoctorId(), schedule.getScheduleDate());

        return appt;
    }

    /**
     * 我的预约列表（最近 N 条）
     */
    public List<Appointment> my(Long userId, int limit) {
        int safeLimit = Math.max(1, Math.min(limit, 100));
        return appointmentMapper.listByUser(userId, safeLimit);
    }

    /**
     * 取消预约（回补号源 + 事务）
     */
    @Transactional
    public void cancel(Long userId, Long appointmentId) {
        Appointment appt = appointmentMapper.findById(appointmentId);
        if (appt == null) {
            throw new ApiException(ErrorCode.NOT_FOUND, "预约不存在");
        }
        if (!userId.equals(appt.getUserId())) {
            throw new ApiException(ErrorCode.FORBIDDEN, "只能取消自己的预约");
        }
        if (!"BOOKED".equals(appt.getStatus())) {
            throw new ApiException(ErrorCode.CONFLICT, "该预约已取消或不可取消");
        }

        // 1) 把预约状态更新为 CANCELLED（并确保是 BOOKED 状态才能取消）
        int changed = appointmentMapper.cancelByIdAndUser(appointmentId, userId);
        if (changed != 1) {
            throw new ApiException(ErrorCode.CONFLICT, "取消失败，请重试");
        }

        // 2) 回补号源
        scheduleMapper.increaseQuotaIfPossible(appt.getScheduleId());

        // 3) 缓存失效
        DoctorSchedule schedule = scheduleMapper.findById(appt.getScheduleId());
        if (schedule != null) {
            scheduleService.evictCache(schedule.getDoctorId(), schedule.getScheduleDate());
        }
    }
}

