package com.shakhawat.myedu.repository;

import com.shakhawat.myedu.model.Teacher;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherRepository extends MongoRepository<Teacher, String> {
    List<Teacher> findByDepartmentId(String departmentId);
    Optional<Teacher> findByEmail(String email);
    boolean existsByEmail(String email);
    long countByDepartmentId(String departmentId);
}
