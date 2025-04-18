package com.shakhawat.myedu.controller;

import com.shakhawat.myedu.dto.UniversityDTO;
import com.shakhawat.myedu.dto.response.ApiResponse;
import com.shakhawat.myedu.service.UniversityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/universities")
@RequiredArgsConstructor
@Tag(name = "University", description = "University management APIs")
@SecurityRequirement(name = "bearerAuth")
public class UniversityController {

    private final UniversityService universityService;

    @GetMapping
    @Operation(summary = "Get all universities")
    public ResponseEntity<ApiResponse<List<UniversityDTO>>> getAllUniversities() {
        return ResponseEntity.ok(ApiResponse.success(universityService.getAllUniversities()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get university by ID")
    public ResponseEntity<ApiResponse<UniversityDTO>> getUniversityById(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.success(universityService.getUniversityById(id)));
    }

    @GetMapping("/name/{name}")
    @Operation(summary = "Get university by Name")
    public ResponseEntity<ApiResponse<UniversityDTO>> getUniversityByName(@PathVariable String name) {
        return ResponseEntity.ok(ApiResponse.success(universityService.getUniversityByName(name)));
    }

    @PostMapping
    @Operation(summary = "Create a new university")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse<UniversityDTO>> createUniversity(@Valid @RequestBody UniversityDTO universityDTO) {
        UniversityDTO createdUniversity = universityService.createUniversity(universityDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("University created successfully", createdUniversity));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update university")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse<UniversityDTO>> updateUniversity(
            @PathVariable String id, @Valid @RequestBody UniversityDTO universityDTO) {
        UniversityDTO updatedUniversity = universityService.updateUniversity(id, universityDTO);
        return ResponseEntity.ok(ApiResponse.success("University updated successfully", updatedUniversity));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete university")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteUniversity(@PathVariable String id) {
        universityService.deleteUniversity(id);
        return ResponseEntity.ok(ApiResponse.success("University deleted successfully", null));
    }
}
