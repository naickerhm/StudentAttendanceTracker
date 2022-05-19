package com.naicker.we.attend.data.models;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import org.apache.catalina.filters.AddDefaultCharsetFilter;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Objects;
import java.util.Set;

//Instruct MYSQL that Student class will be a table in the db
@Entity
//bidirectional relationships and infinite recursion
@JsonIdentityInfo(
  generator = ObjectIdGenerators.PropertyGenerator.class, 
  property = "id")
public class Student {
    //Instruct MYSQL that id will be unique for each user
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    
    @Column(
    columnDefinition = "int"
)private Integer id;
    @Column
    private String firstName;
    @Column
    private String lastName;
    @Column(unique=true)
    private String username;
    @Column
    private String email;
    @Column
    private Campus campus;
    @Column
    private int cohortYear;
    @Column
    private boolean dismissed;
    
    @OneToMany
    // @JsonManagedReference
    private Set<AttendanceData> attendance;

    public Student(String username, String firstName, String lastName, String campus, String cohortYear, String email) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.campus = Campus.valueOf(campus);
        this.cohortYear = Integer.valueOf(cohortYear);
        this.email = email;

    }

    public Student(){

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Campus getCampus() {
        return campus;
    }

    public void setCampus(Campus campus) {
        this.campus = campus;
    }

    public int getCohortYear() {
        return cohortYear;
    }

    public void setCohortYear(int cohortYear) {
        this.cohortYear = cohortYear;
    }

    public boolean isDismissed() {
        return dismissed;
    }

    public void setDismissed(boolean dismissed) {
        this.dismissed = dismissed;
    }

    public Set<AttendanceData> getAttendance() {
        return attendance;
    }

    public void setAttendance(Set<AttendanceData> attendance) {
        this.attendance = attendance;
    }

    public int getAttendanceDays(LocalDate startDate, LocalDate endDate) {
        HashMap<LocalDate, Boolean> hm = new HashMap<LocalDate, Boolean>();
        
        // date range att
        if(startDate != null && endDate != null){
            for(AttendanceData attData: this.attendance){
                if (attData.getDateTime().toLocalDate().isAfter(startDate) && attData.getDateTime().toLocalDate().isBefore(endDate)){
                    hm.put(attData.getDateTime().toLocalDate(), true);
                }
            }
        }
        // total attendance
        else {
            for(AttendanceData attData: this.attendance){
                hm.put(attData.getDateTime().toLocalDate(), true);
            }
        }

        return hm.keySet().size();
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", campus=" + campus +
                ", cohortYear=" + cohortYear +
                ", dismissed=" + dismissed +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(id, student.id) && Objects.equals(firstName, student.firstName) && Objects.equals(lastName, student.lastName) && Objects.equals(username, student.username) && Objects.equals(email, student.email) && campus == student.campus&& Objects.equals(email, student.email) && cohortYear == student.cohortYear && dismissed == student.dismissed;
    }
    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, username, email, campus, cohortYear, dismissed);
    }
}
