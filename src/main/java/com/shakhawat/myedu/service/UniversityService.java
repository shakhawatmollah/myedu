package com.shakhawat.myedu.service;

import com.shakhawat.myedu.dto.UniversityDTO;
import com.shakhawat.myedu.exception.ResourceNotFoundException;
import com.shakhawat.myedu.model.University;
import com.shakhawat.myedu.repository.UniversityRepository;
import com.shakhawat.myedu.util.MapperUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UniversityService {

    private final UniversityRepository universityRepository;
    private final MapperUtils mapperUtils;

    @Transactional(readOnly = true)
    public List<UniversityDTO> getAllUniversities() {
        return mapperUtils.toUniversityDTOList(universityRepository.findAll());
    }

    @Transactional(readOnly = true)
    public UniversityDTO getUniversityById(String id) {
        University university = universityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("University", "id", id));
        return mapperUtils.toUniversityDTO(university);
    }

    @Transactional(readOnly = true)
    public UniversityDTO getUniversityByName(String name) {
        University university = universityRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("University", "name", name));
        return mapperUtils.toUniversityDTO(university);
    }

    @Transactional
    public UniversityDTO createUniversity(UniversityDTO universityDTO) {
        if (universityRepository.existsByName(universityDTO.getName())) {
            throw new IllegalArgumentException("University with name " + universityDTO.getName() + " already exists");
        }

        University university = mapperUtils.toUniversity(universityDTO);
        university = universityRepository.save(university);
        return mapperUtils.toUniversityDTO(university);
    }

    @Transactional
    public UniversityDTO updateUniversity(String id, UniversityDTO universityDTO) {
        University existingUniversity = universityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("University", "id", id));

        // Check if name exists but is not the same university
        if (!existingUniversity.getName().equals(universityDTO.getName()) &&
                universityRepository.existsByName(universityDTO.getName())) {
            throw new IllegalArgumentException("University with name " + universityDTO.getName() + " already exists");
        }

        existingUniversity.setName(universityDTO.getName());
        existingUniversity.setAddress(universityDTO.getAddress());
        existingUniversity.setWebsite(universityDTO.getWebsite());
        existingUniversity.setDescription(universityDTO.getDescription());
        existingUniversity.setFoundedYear(universityDTO.getFoundedYear());

        existingUniversity = universityRepository.save(existingUniversity);
        return mapperUtils.toUniversityDTO(existingUniversity);
    }

    @Transactional
    public void deleteUniversity(String id) {
        if (!universityRepository.existsById(id)) {
            throw new ResourceNotFoundException("University", "id", id);
        }
        universityRepository.deleteById(id);
    }
}
