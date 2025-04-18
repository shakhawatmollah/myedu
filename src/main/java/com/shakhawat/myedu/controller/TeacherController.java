package com.shakhawat.myedu.controller;

import com.shakhawat.myedu.dto.TeacherDTO;
import com.shakhawat.myedu.dto.response.ApiResponse;
import com.shakhawat.myedu.service.TeacherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teachers")
@RequiredArgsConstructor
@Tag(name = "Teacher", description = "Teacher management APIs")
@SecurityRequirement(name = "bearerAuth")
public class TeacherController {

    private final TeacherService teacherService;

    // ?page=0&size=10&sort=firstName,asc
    @GetMapping
    @Operation(summary = "Get all teachers")
    public ResponseEntity<ApiResponse<Page<TeacherDTO>>> getAllTeachers(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(teacherService.getAllTeachers(pageable)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get teacher by ID")
    public ResponseEntity<ApiResponse<TeacherDTO>> getTeacherById(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.success(teacherService.getTeacherById(id)));
    }

    @GetMapping("/department/{departmentId}")
    @Operation(summary = "Get teachers by department ID")
    public ResponseEntity<ApiResponse<List<TeacherDTO>>> getTeachersByDepartmentId(@PathVariable String departmentId) {
        return ResponseEntity.ok(ApiResponse.success(teacherService.getTeachersByDepartmentId(departmentId)));
    }

    @PostMapping
    @Operation(summary = "Create a new teacher")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse<TeacherDTO>> createTeacher(@Valid @RequestBody TeacherDTO teacherDTO) {
        TeacherDTO createdTeacher = teacherService.createTeacher(teacherDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Teacher created successfully", createdTeacher));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update teacher")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse<TeacherDTO>> updateTeacher(
            @PathVariable String id, @Valid @RequestBody TeacherDTO teacherDTO) {
        TeacherDTO updatedTeacher = teacherService.updateTeacher(id, teacherDTO);
        return ResponseEntity.ok(ApiResponse.success("Teacher updated successfully", updatedTeacher));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete teacher")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteTeacher(@PathVariable String id) {
        teacherService.deleteTeacher(id);
        return ResponseEntity.ok(ApiResponse.success("Teacher deleted successfully", null));
    }
}
