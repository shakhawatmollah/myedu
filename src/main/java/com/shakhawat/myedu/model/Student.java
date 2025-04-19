package com.shakhawat.myedu.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@Schema(description = "Student entity in the university")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "students")
public class Student {
    @Id
    @Schema(description = "The unique identifier of the student", example = "65a1f8a3e4b0e3a7f8a3e4b3")
    private String id;

    @Schema(description = "The unique identifier of the student", example = "STD-0001")
    @NotBlank
    @Indexed(unique = true)
    private String studentId;

    @Schema(description = "First name of the student", example = "Shakhawat")
    @NotBlank
    private String firstName;

    @Schema(description = "Last name of the student", example = "Mollah")
    @NotBlank
    private String lastName;

    @Schema(description = "Date of birth of the student", example = "2000-01-01")
    @NotNull
    private LocalDate dateOfBirth;

    @Schema(description = "Email of the student", example = "shakhawat.mollah@university.edu")
    @Email
    private String email;

    @Schema(description = "Phone number of the student", example = "+1234567890")
    private String phone;

    @Schema(description = "Enrollment date of the student", example = "2025-05-01")
    @NotNull
    private LocalDate enrollmentDate;

    @Schema(description = "ID of the department to which the student belongs", example = "65a1f8a3e4b0e3a7f8a3e4b1")
    @NotBlank
    private String departmentId;

    @Schema(description = "Academic year of the student", example = "2022-2023")
    private String academicYear;

    @Schema(description = "List of subjects taken by the student", example = "[\"65a1f8a3e4b0e3a7f8a3e4b1\", \"65a1f8a3e4b0e3a7f8a3e4b2\"]")
    private Map<String, Double> grades; // Subject ID to grade mapping

    @Schema(description = "Status of the student", example = "true")
    @Builder.Default
    private Boolean isActive = true;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
