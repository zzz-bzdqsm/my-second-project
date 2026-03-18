package com.example.appointment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 应用入口（Spring Boot 启动类）
 *
 * 你运行它之后：
 * - 后端 API 会启动在默认 8080 端口
 * - static/index.html 会作为默认页面（访问 http://localhost:8081/ 这样的效果）
 */
@SpringBootApplication
public class AppointmentRegistrationApplication {
    public static void main(String[] args) {
        SpringApplication.run(AppointmentRegistrationApplication.class, args);
    }
}

