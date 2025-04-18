package com.shakhawat.myedu.repository;

import com.shakhawat.myedu.model.Department;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends MongoRepository<Department, String> {
    List<Department> findByUniversityId(String universityId);
    Optional<Department> findByUniversityIdAndName(String universityId, String name);
    boolean existsByUniversityIdAndName(String universityId, String name);
}
