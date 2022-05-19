package com.naicker.we.attend.data.repository;

import java.util.List;

import com.naicker.we.attend.data.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepo extends JpaRepository<Student, Integer> {

    //find student by a certain keyword in search bar on all.html
    @Query(value="select * from student s where s.first_name like %:keyword% or s.last_name like %:keyword% or s.username like %:keyword%" , nativeQuery=true)
    List<Student> findByKeyWord(@Param("keyword") String keyword);

    @Query(value="select username from Student student")
    List<String> findUsernames();

    // SELECT * from crudusers.student where username = 'hnaicker';
    @Query(value="select * from student where username = :username" , nativeQuery=true)
    Student findByUsername(@Param("username") String username);
}
