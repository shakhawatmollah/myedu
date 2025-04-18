package com.shakhawat.myedu.util;

import com.shakhawat.myedu.dto.DepartmentDTO;
import com.shakhawat.myedu.dto.StudentDTO;
import com.shakhawat.myedu.dto.TeacherDTO;
import com.shakhawat.myedu.dto.UniversityDTO;
import com.shakhawat.myedu.model.Department;
import com.shakhawat.myedu.model.Student;
import com.shakhawat.myedu.model.Teacher;
import com.shakhawat.myedu.model.University;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MapperUtils {

    private final ModelMapper modelMapper;

    // University mappings
    public UniversityDTO toUniversityDTO(University university) {
        return modelMapper.map(university, UniversityDTO.class);
    }

    public University toUniversity(UniversityDTO universityDTO) {
        return modelMapper.map(universityDTO, University.class);
    }

    public List<UniversityDTO> toUniversityDTOList(List<University> universities) {
        return universities.stream()
                .map(this::toUniversityDTO)
                .collect(Collectors.toList());
    }

    // Department mappings
    public DepartmentDTO toDepartmentDTO(Department department) {
        return modelMapper.map(department, DepartmentDTO.class);
    }

    public Department toDepartment(DepartmentDTO departmentDTO) {
        return modelMapper.map(departmentDTO, Department.class);
    }

    public List<DepartmentDTO> toDepartmentDTOList(List<Department> departments) {
        return departments.stream()
                .map(this::toDepartmentDTO)
                .collect(Collectors.toList());
    }

    // Teacher mappings
    public TeacherDTO toTeacherDTO(Teacher teacher) {
        return modelMapper.map(teacher, TeacherDTO.class);
    }

    public Teacher toTeacher(TeacherDTO teacherDTO) {
        return modelMapper.map(teacherDTO, Teacher.class);
    }

    public List<TeacherDTO> toTeacherDTOList(List<Teacher> teachers) {
        return teachers.stream()
                .map(this::toTeacherDTO)
                .collect(Collectors.toList());
    }

    public Page<TeacherDTO> toTeacherDTOPage(Page<Teacher> teachers) {

        List<TeacherDTO> studentDTOList = teachers.getContent().stream()
                .map(this::toTeacherDTO)
                .collect(Collectors.toList());

        return new PageImpl<>(
                studentDTOList,
                teachers.getPageable(),
                teachers.getTotalElements()
        );
    }

    // Student mappings
    public StudentDTO toStudentDTO(Student student) {
        return modelMapper.map(student, StudentDTO.class);
    }

    public Student toStudent(StudentDTO studentDTO) {
        return modelMapper.map(studentDTO, Student.class);
    }

    public List<StudentDTO> toStudentDTOList(List<Student> students) {
        return students.stream()
                .map(this::toStudentDTO)
                .collect(Collectors.toList());
    }

    // Generic mapping method
    public <S, T> T map(S source, Class<T> targetClass) {
        return modelMapper.map(source, targetClass);
    }

    public <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
        return source.stream()
                .map(element -> modelMapper.map(element, targetClass))
                .collect(Collectors.toList());
    }
}
