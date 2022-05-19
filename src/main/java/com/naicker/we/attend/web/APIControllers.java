package com.naicker.we.attend.web;

import com.naicker.we.attend.data.models.Student;
import com.naicker.we.attend.data.payloads.DateRangeRequest;
import com.naicker.we.attend.data.payloads.MessageResponse;
import com.naicker.we.attend.data.payloads.StudentRequest;
import com.naicker.we.attend.exception.ResourceNotFoundException;
import com.naicker.we.attend.service.AttendanceService;
import com.naicker.we.attend.service.StudentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;



@RestController
//@RequestMapping("/student")
//
//@ApiResponses(value = {
//        @io.swagger.annotations.ApiResponse(code = 400, message = "This is a bad request, please follow the API documentation for the proper request format"),
//        @io.swagger.annotations.ApiResponse(code = 401, message = "Due to security constraints, your access request cannot be authorized"),
//        @io.swagger.annotations.ApiResponse(code = 500, message = "The server is down. Please bear with us."),
//}
//)

public class APIControllers {

    @Autowired
    StudentService studentService;

    @Autowired
    AttendanceService attService;

    @GetMapping(value="/all")
    @ResponseBody
    public Model getAllStudents (Model model, @RequestParam(required = false) String keyword) {
        List<Student> students = studentService.getAllStudent();

        if(keyword != null){
            model.addAttribute("students", studentService.findByKeyword(keyword));
        }
        else{
            model.addAttribute("students", students);
        }
        
        return model;
    }

    @PostMapping("/add")
    public ResponseEntity<MessageResponse> addStudents(@RequestBody StudentRequest student) {
        MessageResponse newStudent = studentService.createStudent(student);
        return new ResponseEntity<>(newStudent, HttpStatus.CREATED);
    }


    @PostMapping(value ="/update", consumes={MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    @ResponseBody
    public Optional<Student> updateStudent(@RequestParam(name = "studentId") String studentId, @ModelAttribute StudentRequest student) {
        System.out.println("id = "+studentId);
        System.out.println("student= "+student);
        Integer id = Integer.valueOf(studentId);
        return studentService.updateStudent(id, student);
    }

    @GetMapping("/dismiss")
    public ResponseEntity<?> deleteStudents(@RequestParam(name="studentId") int id) {
        try {
            Student studentForDelete = studentService.getASingleStudent(id);
            
            studentService.updateStudentDismissed(studentForDelete);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @RequestMapping("/attendance")
    public Model getAttendanceStats(Model model, @ModelAttribute DateRangeRequest daterange){
        List<Student> students = studentService.getAllStudent();

        for(Student std: students){
            std.setAttendance(attService.findByStudent(std.getId()));
        }

        LocalDate startDate = LocalDate.of(2020,1,1);
        LocalDate endDate = LocalDate.now();

        System.out.println("date range param: " + daterange);

        if (daterange.startDate != null) {
            startDate = daterange.startDate;
        }

        if(daterange.endDate != null) {
            endDate = daterange.endDate;
        }

        System.out.println("model dates, start: " + startDate + ", end: " + endDate);

        model.addAttribute("students", students);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);

        return model;        
    }
}


