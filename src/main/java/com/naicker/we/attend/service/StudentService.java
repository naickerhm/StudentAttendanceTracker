package com.naicker.we.attend.service;

import com.naicker.we.attend.data.models.Student;
import com.naicker.we.attend.data.payloads.MessageResponse;
import com.naicker.we.attend.data.payloads.StudentRequest;
import com.naicker.we.attend.exception.ResourceNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface StudentService {
    MessageResponse createStudent(StudentRequest studentRequest);
    Optional<Student> findById(Integer id);
    Optional<Student> updateStudent(Integer studentId, StudentRequest studentRequest);
    void deleteStudent(Integer studentId) throws ResourceNotFoundException;
    Student getASingleStudent(Integer studentId);
    List<Student> getAllStudent();
    List<Student> findByKeyword(String keyword);
    Student findByUsername(String username);
    boolean updateStudentDismissed(Student student);
}
