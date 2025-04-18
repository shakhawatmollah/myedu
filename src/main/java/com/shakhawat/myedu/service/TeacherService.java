package com.shakhawat.myedu.service;

import com.shakhawat.myedu.dto.TeacherDTO;
import com.shakhawat.myedu.exception.ResourceNotFoundException;
import com.shakhawat.myedu.model.Department;
import com.shakhawat.myedu.model.Teacher;
import com.shakhawat.myedu.repository.DepartmentRepository;
import com.shakhawat.myedu.repository.TeacherRepository;
import com.shakhawat.myedu.util.MapperUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final TeacherRepository teacherRepository;
    private final DepartmentRepository departmentRepository;
    private final MapperUtils mapperUtils;

    @Transactional(readOnly = true)
    public Page<TeacherDTO> getAllTeachers(Pageable pageable) {
       // List<TeacherDTO> teachers = mapperUtils.toTeacherDTOList(teacherRepository.findAll());
        return mapperUtils.toTeacherDTOPage(teacherRepository.findAll(pageable));
    }

    @Transactional(readOnly = true)
    public List<TeacherDTO> getTeachersByDepartmentId(String departmentId) {
        if (!departmentRepository.existsById(departmentId)) {
            throw new ResourceNotFoundException("Department", "id", departmentId);
        }

        return mapperUtils.toTeacherDTOList(teacherRepository.findByDepartmentId(departmentId));
    }

    @Transactional(readOnly = true)
    public TeacherDTO getTeacherById(String id) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher", "id", id));
        return mapperUtils.toTeacherDTO(teacher);
    }

    @Transactional
    public TeacherDTO createTeacher(TeacherDTO teacherDTO) {
        // Verify department exists
        Department department = departmentRepository.findById(teacherDTO.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", teacherDTO.getDepartmentId()));

        // Check if email already exists
        if (teacherRepository.existsByEmail(teacherDTO.getEmail())) {
            throw new IllegalArgumentException("Teacher with email " + teacherDTO.getEmail() + " already exists");
        }

        Teacher teacher = mapperUtils.toTeacher(teacherDTO);
        teacher = teacherRepository.save(teacher);

        // Update department with new teacher
        if (department.getTeacherIds() == null || !department.getTeacherIds().contains(teacher.getId())) {
            if (department.getTeacherIds() == null) {
                department.setTeacherIds(List.of(teacher.getId()));
            } else {
                department.getTeacherIds().add(teacher.getId());
            }
            departmentRepository.save(department);
        }

        return mapperUtils.toTeacherDTO(teacher);
    }

    @Transactional
    public TeacherDTO updateTeacher(String id, TeacherDTO teacherDTO) {
        Teacher existingTeacher = teacherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher", "id", id));

        // Check if email exists but is not the same teacher
        if (!existingTeacher.getEmail().equals(teacherDTO.getEmail()) &&
                teacherRepository.existsByEmail(teacherDTO.getEmail())) {
            throw new IllegalArgumentException("Teacher with email " + teacherDTO.getEmail() + " already exists");
        }

        // If department changed, update the department references
        if (!existingTeacher.getDepartmentId().equals(teacherDTO.getDepartmentId())) {
            // Remove from old department
            Teacher finalExistingTeacher = existingTeacher;
            Department oldDepartment = departmentRepository.findById(existingTeacher.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department", "id", finalExistingTeacher.getDepartmentId()));

            oldDepartment.getTeacherIds().remove(id);
            departmentRepository.save(oldDepartment);

            // Add to new department
            Department newDepartment = departmentRepository.findById(teacherDTO.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department", "id", teacherDTO.getDepartmentId()));

            if (newDepartment.getTeacherIds() == null) {
                newDepartment.setTeacherIds(List.of(id));
            } else {
                newDepartment.getTeacherIds().add(id);
            }
            departmentRepository.save(newDepartment);
        }

        existingTeacher.setFirstName(teacherDTO.getFirstName());
        existingTeacher.setLastName(teacherDTO.getLastName());
        existingTeacher.setDateOfBirth(teacherDTO.getDateOfBirth());
        existingTeacher.setEmail(teacherDTO.getEmail());
        existingTeacher.setPhone(teacherDTO.getPhone());
        existingTeacher.setQualification(teacherDTO.getQualification());
        existingTeacher.setJoinDate(teacherDTO.getJoinDate());
        existingTeacher.setDepartmentId(teacherDTO.getDepartmentId());
        existingTeacher.setSubjectsTaught(teacherDTO.getSubjectsTaught());
        existingTeacher.setIsActive(teacherDTO.getIsActive());

        existingTeacher = teacherRepository.save(existingTeacher);
        return mapperUtils.toTeacherDTO(existingTeacher);
    }

    @Transactional
    public void deleteTeacher(String id) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher", "id", id));

        // Remove from department
        Department department = departmentRepository.findById(teacher.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", teacher.getDepartmentId()));

        if (department.getTeacherIds() != null) {
            department.getTeacherIds().remove(id);
            departmentRepository.save(department);
        }

        teacherRepository.deleteById(id);
    }
}
