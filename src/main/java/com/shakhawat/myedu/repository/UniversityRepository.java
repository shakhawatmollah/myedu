package com.shakhawat.myedu.repository;


import com.shakhawat.myedu.model.University;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UniversityRepository extends MongoRepository<University, String> {
    Optional<University> findByName(String name);
    boolean existsByName(String name);
}
