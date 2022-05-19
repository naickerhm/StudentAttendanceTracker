package com.naicker.we.attend.service;

import com.naicker.we.attend.data.models.Student;
import com.naicker.we.attend.data.repository.StudentRepo;
import com.naicker.we.attend.helper.CSVHelper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public class CSVService {
    @Autowired
    StudentRepo repository;

    public void save(MultipartFile file) {
        try {
            List<Student> students = CSVHelper.csvToStudents(file.getInputStream());

            List<Student> studentsInDB = repository.findAll();

            List<Student> newStudentsToAdd = new ArrayList<Student>();

            for(Student student: students){
                if(!studentsInDB.stream().map(Student::getUsername)
                .filter(student.getUsername()::equals).findFirst().isPresent()){
                    newStudentsToAdd.add(student);
                }
            }

            repository.saveAll(newStudentsToAdd);
        } catch (IOException e) {
            throw new RuntimeException("fail to store csv data: " + e.getMessage());
        }
    }
    // SELECT * FROM crudusers.student;

    public List<Student> getAllStudents() {
        return repository.findAll();
    }
}
