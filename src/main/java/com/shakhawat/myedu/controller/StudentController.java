package com.shakhawat.myedu.controller;

import com.shakhawat.myedu.dto.StudentDTO;
import com.shakhawat.myedu.dto.response.ApiResponse;
import com.shakhawat.myedu.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<StudentDTO>>> getAllStudents() {
        return ResponseEntity.ok(ApiResponse.success(studentService.getAllStudents()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentDTO>> getStudentById(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.success(studentService.getStudentById(id)));
    }

    @GetMapping("/studentId/{studentId}")
    public ResponseEntity<ApiResponse<StudentDTO>> getStudentByStudentId(@PathVariable String studentId) {
        return ResponseEntity.ok(ApiResponse.success(studentService.getStudentByStudentId(studentId)));
    }

    @GetMapping("/department/{departmentId}")
    public ResponseEntity<ApiResponse<List<StudentDTO>>> getStudentsByDepartmentId(@PathVariable String departmentId) {
        return ResponseEntity.ok(ApiResponse.success(studentService.getStudentsByDepartmentId(departmentId)));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse<StudentDTO>> createStudent(@Valid @RequestBody StudentDTO studentDTO) {
        StudentDTO createdStudent = studentService.createStudent(studentDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Student created successfully", createdStudent));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse<StudentDTO>> updateStudent(
            @PathVariable String id, @Valid @RequestBody StudentDTO studentDTO) {
        StudentDTO updatedStudent = studentService.updateStudent(id, studentDTO);
        return ResponseEntity.ok(ApiResponse.success("Student updated successfully", updatedStudent));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteStudent(@PathVariable String id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok(ApiResponse.success("Student deleted successfully", null));
    }

    @PatchMapping("/{id}/grades")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<ApiResponse<StudentDTO>> updateStudentGrades(
            @PathVariable String id, @RequestBody Map<String, Double> grades) {
        StudentDTO updatedStudent = studentService.updateStudentGrades(id, grades);
        return ResponseEntity.ok(ApiResponse.success("Student grades updated successfully", updatedStudent));
    }
}
