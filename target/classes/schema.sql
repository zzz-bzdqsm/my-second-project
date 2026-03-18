/*
  MySQL schema for AppointmentRegistration (MVP)

  说明：
  - 这份脚本用于“从 0 快速跑起来”
  - 所以都用了 IF NOT EXISTS，重复执行不会报错，也不会删除已有数据
*/

CREATE TABLE IF NOT EXISTS users (
  id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  username VARCHAR(64) NOT NULL COMMENT '登录用户名（唯一）',
  password_hash VARCHAR(255) NOT NULL COMMENT 'BCrypt 加密后的密码',
  phone VARCHAR(32) NULL COMMENT '手机号（可选）',
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (id),
  UNIQUE KEY uk_users_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';

CREATE TABLE IF NOT EXISTS doctors (
  id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  name VARCHAR(64) NOT NULL COMMENT '医生姓名',
  dept VARCHAR(64) NOT NULL COMMENT '科室（例如 内科/外科/儿科）',
  title VARCHAR(64) NULL COMMENT '职称（例如 主治医师）',
  intro VARCHAR(512) NULL COMMENT '简介（可选）',
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (id),
  KEY idx_doctors_dept (dept)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='医生表';

CREATE TABLE IF NOT EXISTS doctor_schedule (
  id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  doctor_id BIGINT NOT NULL COMMENT '医生ID',
  schedule_date DATE NOT NULL COMMENT '日期',
  time_slot VARCHAR(8) NOT NULL COMMENT '时间段：AM/PM',
  total_quota INT NOT NULL COMMENT '总号源',
  available_quota INT NOT NULL COMMENT '剩余号源',
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (id),
  UNIQUE KEY uk_schedule_doctor_date_slot (doctor_id, schedule_date, time_slot),
  KEY idx_schedule_doctor_date (doctor_id, schedule_date),
  CONSTRAINT fk_schedule_doctor FOREIGN KEY (doctor_id) REFERENCES doctors(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='医生排班/号源';

CREATE TABLE IF NOT EXISTS appointments (
  id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  user_id BIGINT NOT NULL COMMENT '用户ID',
  doctor_id BIGINT NOT NULL COMMENT '医生ID（冗余一份，方便查询）',
  schedule_id BIGINT NOT NULL COMMENT '排班ID',
  status VARCHAR(16) NOT NULL COMMENT 'BOOKED/CANCELLED',
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  cancelled_at TIMESTAMP NULL COMMENT '取消时间',
  PRIMARY KEY (id),
  KEY idx_appointments_user_id (user_id),
  KEY idx_appointments_schedule_id (schedule_id),
  CONSTRAINT fk_appointments_user FOREIGN KEY (user_id) REFERENCES users(id),
  CONSTRAINT fk_appointments_doctor FOREIGN KEY (doctor_id) REFERENCES doctors(id),
  CONSTRAINT fk_appointments_schedule FOREIGN KEY (schedule_id) REFERENCES doctor_schedule(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='预约记录';

