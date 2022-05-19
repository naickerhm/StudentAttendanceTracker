package com.naicker.we.attend.data.models;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.Id;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
//bidirectional relationships and infinite recursion
@JsonIdentityInfo(
  generator = ObjectIdGenerators.PropertyGenerator.class, 
  property = "id")
public class AttendanceData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    @DateTimeFormat(pattern = "yyyy-MM-dd HH-mm")
    private LocalDateTime dateTime;

    @ManyToOne
    @JoinColumn(name="studentid", insertable = false,updatable = false)
    //bidirectional relationships and infinite recursion
    // @JsonBackReference
    private Student student;

    //foreign key
    // @Column(unique=true)
    private Integer studentid;


    public AttendanceData(LocalDateTime dateTime, Student student) {
        this.dateTime = dateTime;
        this.student = student;
        this.studentid = student.getId();
    }

    public AttendanceData(){
        
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Integer getStudentid() {
        return studentid;
    }

    public void setStudentid(Integer studentid) {
        this.studentid = studentid;
    }

    @Override
    public String toString() {
        return "AttendanceData{" +
                "id=" + id +
                ", dateTime=" + dateTime +
                ", student=" + student +
                ", studentid=" + studentid +
                '}';
    }
}
