package com.naicker.we.attend.service;

import com.naicker.we.attend.data.models.Student;
import com.naicker.we.attend.data.payloads.MessageResponse;
import com.naicker.we.attend.data.payloads.StudentRequest;
import com.naicker.we.attend.data.repository.StudentRepo;
import com.naicker.we.attend.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService{
    @Autowired
    StudentRepo studentRepo;

    @Override
    public MessageResponse createStudent(StudentRequest studentRequest) {
        Student student = new Student();
        student.setFirstName(studentRequest.getFirstName());
        student.setLastName(studentRequest.getLastname());
        student.setUsername(studentRequest.getUsername());
        student.setEmail(studentRequest.getEmail());
        student.setCohortYear(studentRequest.getCohortYear());
        student.setCampus(studentRequest.getCampus());
        student.setDismissed(studentRequest.isDismissed());
        studentRepo.save(student);
        return new MessageResponse("New Student created successfully");
    }

    //Get Student By Id
    
	public Optional<Student> findById(Integer studentId) {
		return studentRepo.findById(studentId);
	}	

    @Override
    public Optional<Student> updateStudent(Integer studentId, StudentRequest studentRequest) throws ResourceNotFoundException{
        Optional<Student> student = studentRepo.findById(studentId);
        if (student.isEmpty()){
            throw new ResourceNotFoundException("Student", "id", studentId);
        }
        else
            student.get().setFirstName(studentRequest.getFirstName());
            student.get().setLastName(studentRequest.getLastname());
            student.get().setUsername(studentRequest.getUsername());
            student.get().setEmail(studentRequest.getEmail());
            student.get().setCohortYear(studentRequest.getCohortYear());
            student.get().setCampus(studentRequest.getCampus());
            studentRepo.save(student.get());
        return student;
    }

    @Override
    public boolean updateStudentDismissed(Student student){
        student.setDismissed(true);
        return studentRepo.save(student) != null;
    }

    @Override
    public void deleteStudent(Integer studentId) throws ResourceNotFoundException{
        if (studentRepo.getById(studentId).getId().equals(studentId)){
            studentRepo.deleteById(studentId);
        }
        else throw new ResourceNotFoundException("Student", "id", studentId);

    }

    @Override
    public Student getASingleStudent(Integer studentId) throws ResourceNotFoundException{
        return studentRepo.findById(studentId).orElseThrow(() -> new ResourceNotFoundException("Student", "id", studentId));
    }

    @Override
    public List<Student> getAllStudent() {
        return studentRepo.findAll();
    }

    //Get student by keyword
    @Override
    public List<Student> findByKeyword(String keyword) {
        return studentRepo.findByKeyWord(keyword);
    }

    @Override
    public Student findByUsername(String username) {
        return studentRepo.findByUsername(username);
    }

}
