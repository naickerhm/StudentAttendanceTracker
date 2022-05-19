package com.naicker.we.attend.service;

import java.util.List;
import java.util.Set;

import com.naicker.we.attend.data.models.AttendanceData;
import com.naicker.we.attend.data.repository.AttendanceRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    @Autowired
    AttendanceRepo attendanceRepo;

    @Override
    public List<AttendanceData> getAttendanceForAll() {
        return null;
    }

    @Override
    public List<AttendanceData> findByKeyword(String keyword) {
        return null;
    }

    @Override
    public Set<AttendanceData> findByStudent(int studentId) {
        return attendanceRepo.findByStudent(studentId);
    }
    
}
