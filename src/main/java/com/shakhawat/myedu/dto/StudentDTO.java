package com.shakhawat.myedu.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentDTO {
    private String id;

    @NotBlank(message = "Student ID is required")
    private String studentId;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotNull(message = "Date of birth is required")
    private LocalDate dateOfBirth;

    @Email(message = "Email must be valid")
    private String email;

    private String phone;

    @NotNull(message = "Enrollment date is required")
    private LocalDate enrollmentDate;

    @NotBlank(message = "Department ID is required")
    private String departmentId;

    private String academicYear;

    private Map<String, Double> grades;

    private Boolean isActive;
}
