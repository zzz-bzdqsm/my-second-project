/*
  演示数据（可选）
  - 目的是：启动后立刻能在前端看到医生和排班
  - 使用 INSERT ... ON DUPLICATE KEY UPDATE，重复执行不会报错
*/

INSERT INTO doctors (id, name, dept, title, intro)
VALUES
  (1, '张医生', '内科', '主治医师', '擅长常见内科疾病诊疗'),
  (2, '李医生', '外科', '副主任医师', '擅长普外科常见手术'),
  (3, '王医生', '儿科', '主治医师', '擅长儿童常见病诊疗')
ON DUPLICATE KEY UPDATE
  name=VALUES(name), dept=VALUES(dept), title=VALUES(title), intro=VALUES(intro);

INSERT INTO doctor_schedule (id, doctor_id, schedule_date, time_slot, total_quota, available_quota)
VALUES
  (1, 1, CURDATE(), 'AM', 10, 10),
  (2, 1, CURDATE(), 'PM', 10, 10),
  (3, 2, CURDATE(), 'AM', 8, 8),
  (4, 2, CURDATE(), 'PM', 8, 8),
  (5, 3, CURDATE(), 'AM', 12, 12),
  (6, 3, CURDATE(), 'PM', 12, 12)
ON DUPLICATE KEY UPDATE
  total_quota=VALUES(total_quota),
  available_quota=VALUES(available_quota);

