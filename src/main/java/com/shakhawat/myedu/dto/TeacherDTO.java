package com.shakhawat.myedu.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherDTO {
    private String id;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotNull(message = "Date of birth is required")
    private LocalDate dateOfBirth;

    @Email(message = "Email must be valid")
    private String email;

    private String phone;

    @NotBlank(message = "Qualification is required")
    private String qualification;

    @NotNull(message = "Join date is required")
    private LocalDate joinDate;

    @NotBlank(message = "Department ID is required")
    private String departmentId;

    private List<String> subjectsTaught;

    private Boolean isActive;
}
