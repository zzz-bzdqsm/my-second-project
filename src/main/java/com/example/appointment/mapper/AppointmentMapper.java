package com.example.appointment.mapper;

import com.example.appointment.model.Appointment;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 预约记录 Mapper
 */
@Mapper
public interface AppointmentMapper {

    @Insert("""
            INSERT INTO appointments(user_id, doctor_id, schedule_id, status)
            VALUES(#{userId}, #{doctorId}, #{scheduleId}, #{status})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Appointment appointment);

    @Select("""
            SELECT id, user_id AS userId, doctor_id AS doctorId, schedule_id AS scheduleId,
                   status, created_at AS createdAt, cancelled_at AS cancelledAt
            FROM appointments
            WHERE user_id = #{userId}
            ORDER BY id DESC
            LIMIT #{limit}
            """)
    List<Appointment> listByUser(@Param("userId") Long userId, @Param("limit") int limit);

    @Select("""
            SELECT id, user_id AS userId, doctor_id AS doctorId, schedule_id AS scheduleId,
                   status, created_at AS createdAt, cancelled_at AS cancelledAt
            FROM appointments
            WHERE id = #{id}
            """)
    Appointment findById(@Param("id") Long id);

    @Select("""
            SELECT id, user_id AS userId, doctor_id AS doctorId, schedule_id AS scheduleId,
                   status, created_at AS createdAt, cancelled_at AS cancelledAt
            FROM appointments
            WHERE user_id = #{userId}
              AND schedule_id = #{scheduleId}
              AND status = 'BOOKED'
            LIMIT 1
            """)
    Appointment findActiveByUserAndSchedule(@Param("userId") Long userId, @Param("scheduleId") Long scheduleId);

    @Update("""
            UPDATE appointments
            SET status = 'CANCELLED', cancelled_at = NOW()
            WHERE id = #{id}
              AND user_id = #{userId}
              AND status = 'BOOKED'
            """)
    int cancelByIdAndUser(@Param("id") Long id, @Param("userId") Long userId);
}

