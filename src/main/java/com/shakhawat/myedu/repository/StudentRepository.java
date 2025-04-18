package com.shakhawat.myedu.repository;

import com.shakhawat.myedu.model.Student;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends MongoRepository<Student, String> {
    List<Student> findByDepartmentId(String departmentId);
    Optional<Student> findByStudentId(String studentId);
    boolean existsByStudentId(String studentId);
    long countByDepartmentId(String departmentId);

    @Query(value = "{ 'departmentId': ?0 }", fields = "{ 'grades': 1 }")
    List<Student> findGradesByDepartmentId(String departmentId);
}
