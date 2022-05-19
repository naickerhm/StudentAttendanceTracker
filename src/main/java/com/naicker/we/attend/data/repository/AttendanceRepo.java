package com.naicker.we.attend.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

import com.naicker.we.attend.data.models.AttendanceData;

@Repository
public interface AttendanceRepo extends JpaRepository<AttendanceData, Integer>{
    
    // SELECT * from crudusers.student where username = 'hnaicker';
    @Query(value="select * from attendance_data where studentid = :studentIdent" , nativeQuery=true)
    Set<AttendanceData> findByStudent(@Param("studentIdent") int studentIdent);
}
