package com.naicker.we.attend.service;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

import com.naicker.we.attend.data.models.AttendanceData;

@Component
public interface AttendanceService {

    List<AttendanceData> getAttendanceForAll();
    List<AttendanceData> findByKeyword(String keyword);
    Set<AttendanceData> findByStudent(int studentId);
    
}
