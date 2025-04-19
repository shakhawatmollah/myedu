package com.shakhawat.myedu.controller;

import com.shakhawat.myedu.dto.UniversityDTO;
import com.shakhawat.myedu.dto.response.ApiResponse;
import com.shakhawat.myedu.model.University;
import com.shakhawat.myedu.service.UniversityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(
            summary = "Get all universities",
            description = "Retrieve a paginated list of all universities"
    )
    @GetMapping
    public ResponseEntity<ApiResponse<List<UniversityDTO>>> getAllUniversities() {
        return ResponseEntity.ok(ApiResponse.success(universityService.getAllUniversities()));
    }

    @Operation(
            summary = "Get a university by ID",
            description = "Retrieve a university by its unique identifier"
    )
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UniversityDTO>> getUniversityById(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.success(universityService.getUniversityById(id)));
    }

    @GetMapping("/name/{name}")
    @Operation(summary = "Get university by Name")
    public ResponseEntity<ApiResponse<UniversityDTO>> getUniversityByName(@PathVariable String name) {
        return ResponseEntity.ok(ApiResponse.success(universityService.getUniversityByName(name)));
    }

    @Operation(
            summary = "Create a new university",
            description = "Create a new university with the given details"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "University created successfully",
                    content = {@Content(schema = @Schema(implementation = University.class))}),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content)
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse<UniversityDTO>> createUniversity(@Valid @RequestBody UniversityDTO universityDTO) {
        UniversityDTO createdUniversity = universityService.createUniversity(universityDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("University created successfully", createdUniversity));
    }

    @Operation(
            summary = "Update a university",
            description = "Update the details of an existing university"
    )
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse<UniversityDTO>> updateUniversity(
            @PathVariable String id, @Valid @RequestBody UniversityDTO universityDTO) {
        UniversityDTO updatedUniversity = universityService.updateUniversity(id, universityDTO);
        return ResponseEntity.ok(ApiResponse.success("University updated successfully", updatedUniversity));
    }

    @Operation(
            summary = "Delete a university",
            description = "Delete a university by its ID"
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteUniversity(@PathVariable String id) {
        universityService.deleteUniversity(id);
        return ResponseEntity.ok(ApiResponse.success("University deleted successfully", null));
    }
}
