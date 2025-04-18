package com.shakhawat.myedu.service;

import com.shakhawat.myedu.dto.StudentDTO;
import com.shakhawat.myedu.exception.ResourceNotFoundException;
import com.shakhawat.myedu.model.Department;
import com.shakhawat.myedu.model.Student;
import com.shakhawat.myedu.repository.DepartmentRepository;
import com.shakhawat.myedu.repository.StudentRepository;
import com.shakhawat.myedu.util.MapperUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final DepartmentRepository departmentRepository;
    private final MapperUtils mapperUtils;

    @Transactional(readOnly = true)
    public List<StudentDTO> getAllStudents() {
        return mapperUtils.toStudentDTOList(studentRepository.findAll());
    }

    @Transactional(readOnly = true)
    public List<StudentDTO> getStudentsByDepartmentId(String departmentId) {
        if (!departmentRepository.existsById(departmentId)) {
            throw new ResourceNotFoundException("Department", "id", departmentId);
        }

        return mapperUtils.toStudentDTOList(studentRepository.findByDepartmentId(departmentId));
    }

    @Transactional(readOnly = true)
    public StudentDTO getStudentById(String id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", id));
        return mapperUtils.toStudentDTO(student);
    }

    @Transactional(readOnly = true)
    public StudentDTO getStudentByStudentId(String studentId) {
        Student student = studentRepository.findByStudentId(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "studentId", studentId));
        return mapperUtils.toStudentDTO(student);
    }

    @Transactional
    public StudentDTO createStudent(StudentDTO studentDTO) {
        // Verify department exists
        Department department = departmentRepository.findById(studentDTO.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", studentDTO.getDepartmentId()));

        // Check if student ID already exists
        if (studentRepository.existsByStudentId(studentDTO.getStudentId())) {
            throw new IllegalArgumentException("Student with ID " + studentDTO.getStudentId() + " already exists");
        }

        Student student = mapperUtils.toStudent(studentDTO);
        student = studentRepository.save(student);

        // Update department with new student
        if (department.getStudentIds() == null || !department.getStudentIds().contains(student.getId())) {
            if (department.getStudentIds() == null) {
                department.setStudentIds(List.of(student.getId()));
            } else {
                department.getStudentIds().add(student.getId());
            }
            departmentRepository.save(department);
        }

        return mapperUtils.toStudentDTO(student);
    }

    @Transactional
    public StudentDTO updateStudent(String id, StudentDTO studentDTO) {
        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", id));

        // Check if student ID exists but is not the same student
        if (!existingStudent.getStudentId().equals(studentDTO.getStudentId()) &&
                studentRepository.existsByStudentId(studentDTO.getStudentId())) {
            throw new IllegalArgumentException("Student with ID " + studentDTO.getStudentId() + " already exists");
        }

        // If department changed, update the department references
        if (!existingStudent.getDepartmentId().equals(studentDTO.getDepartmentId())) {
            // Remove from old department
            Student finalExistingStudent = existingStudent;
            Department oldDepartment = departmentRepository.findById(existingStudent.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department", "id", finalExistingStudent.getDepartmentId()));

            oldDepartment.getStudentIds().remove(id);
            departmentRepository.save(oldDepartment);

            // Add to new department
            Department newDepartment = departmentRepository.findById(studentDTO.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department", "id", studentDTO.getDepartmentId()));

            if (newDepartment.getStudentIds() == null) {
                newDepartment.setStudentIds(List.of(id));
            } else {
                newDepartment.getStudentIds().add(id);
            }
            departmentRepository.save(newDepartment);
        }

        existingStudent.setStudentId(studentDTO.getStudentId());
        existingStudent.setFirstName(studentDTO.getFirstName());
        existingStudent.setLastName(studentDTO.getLastName());
        existingStudent.setDateOfBirth(studentDTO.getDateOfBirth());
        existingStudent.setEmail(studentDTO.getEmail());
        existingStudent.setPhone(studentDTO.getPhone());
        existingStudent.setEnrollmentDate(studentDTO.getEnrollmentDate());
        existingStudent.setDepartmentId(studentDTO.getDepartmentId());
        existingStudent.setAcademicYear(studentDTO.getAcademicYear());
        existingStudent.setGrades(studentDTO.getGrades());
        existingStudent.setIsActive(studentDTO.getIsActive());

        existingStudent = studentRepository.save(existingStudent);
        return mapperUtils.toStudentDTO(existingStudent);
    }

    @Transactional
    public void deleteStudent(String id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", id));

        // Remove from department
        Department department = departmentRepository.findById(student.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", student.getDepartmentId()));

        if (department.getStudentIds() != null) {
            department.getStudentIds().remove(id);
            departmentRepository.save(department);
        }

        studentRepository.deleteById(id);
    }

    @Transactional
    public StudentDTO updateStudentGrades(String id, Map<String, Double> grades) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", id));

        student.setGrades(grades);
        student = studentRepository.save(student);

        return mapperUtils.toStudentDTO(student);
    }
}
