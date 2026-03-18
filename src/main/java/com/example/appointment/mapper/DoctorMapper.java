package com.example.appointment.mapper;

import com.example.appointment.model.Doctor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 医生查询 Mapper
 */
@Mapper
public interface DoctorMapper {

    /**
     * 医生列表：支持按科室过滤、按关键字搜索（姓名/简介）
     *
     * 说明：
     * - 这里为了简单先不做复杂分页插件，面试可说“可以加分页/limit offset”
     */
    @Select("""
            <script>
            SELECT id, name, dept, title, intro, created_at AS createdAt
            FROM doctors
            <where>
              <if test="dept != null and dept != ''">
                dept = #{dept}
              </if>
              <if test="q != null and q != ''">
                AND (name LIKE CONCAT('%', #{q}, '%') OR intro LIKE CONCAT('%', #{q}, '%'))
              </if>
            </where>
            ORDER BY id ASC
            LIMIT #{limit} OFFSET #{offset}
            </script>
            """)
    List<Doctor> list(@Param("dept") String dept,
                      @Param("q") String q,
                      @Param("limit") int limit,
                      @Param("offset") int offset);

    @Select("SELECT id, name, dept, title, intro, created_at AS createdAt FROM doctors WHERE id = #{id}")
    Doctor findById(@Param("id") Long id);

    @Select("SELECT DISTINCT dept FROM doctors ORDER BY dept")
    List<String> listDepts();
}

