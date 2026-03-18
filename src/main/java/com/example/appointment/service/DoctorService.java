package com.example.appointment.service;

import com.example.appointment.exception.ApiException;
import com.example.appointment.exception.ErrorCode;
import com.example.appointment.mapper.DoctorMapper;
import com.example.appointment.model.Doctor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 医生查询业务
 */
@Service
public class DoctorService {

    private final DoctorMapper doctorMapper;

    public DoctorService(DoctorMapper doctorMapper) {
        this.doctorMapper = doctorMapper;
    }

    public List<Doctor> list(String dept, String q, int page, int size) {
        int safeSize = Math.max(1, Math.min(size, 50));
        int safePage = Math.max(1, page);
        int offset = (safePage - 1) * safeSize;
        return doctorMapper.list(dept, q, safeSize, offset);
    }

    public Doctor get(Long id) {
        Doctor d = doctorMapper.findById(id);
        if (d == null) {
            throw new ApiException(ErrorCode.NOT_FOUND, "医生不存在");
        }
        return d;
    }

    public List<String> depts() {
        return doctorMapper.listDepts();
    }
}

