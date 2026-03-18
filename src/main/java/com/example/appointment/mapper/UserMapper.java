package com.example.appointment.mapper;

import com.example.appointment.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * User 的数据库访问（MyBatis 注解 SQL）
 */
@Mapper
public interface UserMapper {

    @Select("SELECT id, username, password_hash AS passwordHash, phone, created_at AS createdAt FROM users WHERE username = #{username}")
    User findByUsername(@Param("username") String username);

    @Select("SELECT id, username, password_hash AS passwordHash, phone, created_at AS createdAt FROM users WHERE id = #{id}")
    User findById(@Param("id") Long id);

    @Insert("INSERT INTO users(username, password_hash, phone) VALUES(#{username}, #{passwordHash}, #{phone})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(User user);
}

