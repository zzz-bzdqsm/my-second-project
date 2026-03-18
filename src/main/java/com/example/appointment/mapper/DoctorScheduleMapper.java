package com.example.appointment.mapper;

import com.example.appointment.model.DoctorSchedule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDate;
import java.util.List;

/**
 * 排班/号源 Mapper
 */
@Mapper
public interface DoctorScheduleMapper {

    @Select("""
            SELECT id, doctor_id AS doctorId, schedule_date AS scheduleDate, time_slot AS timeSlot,
                   total_quota AS totalQuota, available_quota AS availableQuota, created_at AS createdAt
            FROM doctor_schedule
            WHERE doctor_id = #{doctorId}
              AND schedule_date = #{date}
            ORDER BY FIELD(time_slot, 'AM', 'PM'), id
            """)
    List<DoctorSchedule> listByDoctorAndDate(@Param("doctorId") Long doctorId, @Param("date") LocalDate date);

    @Select("""
            SELECT id, doctor_id AS doctorId, schedule_date AS scheduleDate, time_slot AS timeSlot,
                   total_quota AS totalQuota, available_quota AS availableQuota, created_at AS createdAt
            FROM doctor_schedule
            WHERE id = #{id}
            """)
    DoctorSchedule findById(@Param("id") Long id);

    /**
     * 原子扣减（防止超卖的核心）
     * - available_quota > 0 才允许扣减
     * - 返回 1 表示成功，返回 0 表示号源不足
     */
    @Update("""
            UPDATE doctor_schedule
            SET available_quota = available_quota - 1
            WHERE id = #{id}
              AND available_quota > 0
            """)
    int decreaseQuotaIfAvailable(@Param("id") Long id);

    /**
     * 回补号源（取消预约）
     */
    @Update("""
            UPDATE doctor_schedule
            SET available_quota = available_quota + 1
            WHERE id = #{id}
              AND available_quota < total_quota
            """)
    int increaseQuotaIfPossible(@Param("id") Long id);
}

