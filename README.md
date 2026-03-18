# 预约挂号平台系统（校招 MVP）

一个适合校招展示的“预约挂号平台”后端项目，覆盖常见面试能力点：**Spring Boot + MyBatis + MySQL + Redis + JWT**，并提供一个静态页面用于演示联调。

## 功能
- **用户注册/登录（JWT）**：登录后返回 token；预约相关接口需要携带 `Authorization: Bearer <token>`
- **医生查询**：按科室筛选、关键字搜索
- **排班查询（Redis 缓存）**：按医生 + 日期查询 AM/PM 号源（短 TTL + 预约后主动失效）
- **预约创建/取消**：
  - 创建预约：**原子扣减号源**（`available_quota > 0` 才能扣）避免并发超卖
  - 取消预约：回补号源
- **静态演示页**：无需 Node，启动后访问 `/` 即可操作完整流程

## 技术栈
- Java 17
- Spring Boot 3.x
- MyBatis（注解 SQL）
- MySQL
- Redis
- JWT（JJWT）
- Swagger UI（`/swagger-ui.html`，用于接口展示）

## 快速开始（本地运行）

### 1）准备 MySQL
1. 创建数据库：`appointment_registration`
2. 修改配置：`src/main/resources/application.properties`

项目自带建表脚本：
- `src/main/resources/schema.sql`（建表 + 索引）
- `src/main/resources/data.sql`（演示医生/排班数据）

启动时会自动执行（`spring.sql.init.mode=always`）。

### 2）准备 Redis
本机启动 Redis（默认 `localhost:6379`）。

### 3）启动项目
在 `AppointmentRegistration/` 目录执行：

```bash
mvn spring-boot:run
```

默认端口是 **8081**（避免与你另一个项目 8080 冲突）。启动后：
- 演示页面：`http://localhost:8081/`
- Swagger：`http://localhost:8081/swagger-ui.html`

## API 概览

### Auth
- `POST /api/auth/register`
- `POST /api/auth/login` → `{ token }`
- `GET /api/auth/me`（需要 token）

### Doctors & Schedules
- `GET /api/doctors?dept=&q=&page=&size=`
- `GET /api/doctors/{id}`
- `GET /api/doctors/depts`
- `GET /api/schedules?doctorId=&date=YYYY-MM-DD`

### Appointments（需要 token）
- `POST /api/appointments` body: `{ "scheduleId": 1 }`
- `GET /api/appointments/my?limit=50`
- `POST /api/appointments/{id}/cancel`

## 并发防超卖的实现说明（面试可讲）
创建预约时使用单条 SQL 原子扣减：\n
```sql
UPDATE doctor_schedule
SET available_quota = available_quota - 1
WHERE id = ?
  AND available_quota > 0;
```

返回行数为 1 才算成功，否则表示号源不足。\n
并且把“扣减号源 + 插入预约”放在同一个事务里，保证一致性。

