package com.shakhawat.myedu.service;

import com.shakhawat.myedu.dto.DepartmentDTO;
import com.shakhawat.myedu.exception.ResourceNotFoundException;
import com.shakhawat.myedu.model.Department;
import com.shakhawat.myedu.model.University;
import com.shakhawat.myedu.repository.DepartmentRepository;
import com.shakhawat.myedu.repository.UniversityRepository;
import com.shakhawat.myedu.util.MapperUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final UniversityRepository universityRepository;
    private final MapperUtils mapperUtils;

    @Transactional(readOnly = true)
    public List<DepartmentDTO> getAllDepartments() {
        return mapperUtils.toDepartmentDTOList(departmentRepository.findAll());
    }

    @Transactional(readOnly = true)
    public List<DepartmentDTO> getDepartmentsByUniversityId(String universityId) {
        if (!universityRepository.existsById(universityId)) {
            throw new ResourceNotFoundException("University", "id", universityId);
        }

        return mapperUtils.toDepartmentDTOList(departmentRepository.findByUniversityId(universityId));
    }

    @Transactional(readOnly = true)
    public DepartmentDTO getDepartmentById(String id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", id));
        return mapperUtils.toDepartmentDTO(department);
    }

    @Transactional
    public DepartmentDTO createDepartment(DepartmentDTO departmentDTO) {
        // Verify university exists
        University university = universityRepository.findById(departmentDTO.getUniversityId())
                .orElseThrow(() -> new ResourceNotFoundException("University", "id", departmentDTO.getUniversityId()));

        // Check if department already exists in this university
        if (departmentRepository.existsByUniversityIdAndName(
                departmentDTO.getUniversityId(), departmentDTO.getName())) {
            throw new IllegalArgumentException("Department with name " + departmentDTO.getName() +
                    " already exists in this university");
        }

        Department department = mapperUtils.toDepartment(departmentDTO);
        department = departmentRepository.save(department);

        // Update university with new department
        if (university.getDepartmentIds() == null || !university.getDepartmentIds().contains(department.getId())) {
            if (university.getDepartmentIds() == null) {
                university.setDepartmentIds(List.of(department.getId()));
            } else {
                university.getDepartmentIds().add(department.getId());
            }
            universityRepository.save(university);
        }

        return mapperUtils.toDepartmentDTO(department);
    }

    @Transactional
    public DepartmentDTO updateDepartment(String id, DepartmentDTO departmentDTO) {
        Department existingDepartment = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", id));

        // Check if name exists in the university but is not the same department
        if (!existingDepartment.getName().equals(departmentDTO.getName()) &&
                departmentRepository.existsByUniversityIdAndName(
                        departmentDTO.getUniversityId(), departmentDTO.getName())) {
            throw new IllegalArgumentException("Department with name " + departmentDTO.getName() +
                    " already exists in this university");
        }

        existingDepartment.setName(departmentDTO.getName());
        existingDepartment.setDescription(departmentDTO.getDescription());
        existingDepartment.setHeadOfDepartment(departmentDTO.getHeadOfDepartment());

        existingDepartment = departmentRepository.save(existingDepartment);
        return mapperUtils.toDepartmentDTO(existingDepartment);
    }

    @Transactional
    public void deleteDepartment(String id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", id));

        // Remove from university
        University university = universityRepository.findById(department.getUniversityId())
                .orElseThrow(() -> new ResourceNotFoundException("University", "id", department.getUniversityId()));

        university.getDepartmentIds().remove(id);
        universityRepository.save(university);

        departmentRepository.deleteById(id);
    }
}
