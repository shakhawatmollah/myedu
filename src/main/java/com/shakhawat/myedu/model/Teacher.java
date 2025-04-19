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
import java.util.List;

@Schema(description = "Teacher entity in the university")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "teachers")
public class Teacher {
    @Id
    @Schema(description = "The unique identifier of the teacher", example = "65a1f8a3e4b0e3a7f8a3e4b2")
    private String id;

    @Schema(description = "First name of the teacher", example = "Shakhawat")
    @NotBlank
    private String firstName;

    @Schema(description = "Last name of the teacher", example = "Mollah")
    @NotBlank
    private String lastName;

    @NotNull
    private LocalDate dateOfBirth;

    @Schema(description = "Email of the teacher", example = "shakhawat.mollah@university.edu")
    @Email
    @Indexed(unique = true)
    private String email;

    @Schema(description = "Phone number", example = "+1234567890")
    private String phone;

    @Schema(description = "Qualification of the teacher", example = "Artificial Intelligence")
    @NotBlank
    private String qualification;

    @Schema(description = "Join date of the teacher", example = "2025-05-01")
    @NotNull
    private LocalDate joinDate;

    @Schema(description = "ID of the department to which the teacher belongs", example = "65a1f8a3e4b0e3a7f8a3e4b1")
    @NotBlank
    private String departmentId;

    @Schema(description = "List of subjects taught by the teacher")
    private List<String> subjectsTaught;

    @Schema(description = "Status of the teacher", example = "true")
    @Builder.Default
    private Boolean isActive = true;

    @Schema(description = "Creation date of the teacher record", example = "2025-05-01T10:00:00.000")
    @CreatedDate
    private LocalDateTime createdAt;

    @Schema(description = "Last updated date of the teacher record", example = "2025-05-01T10:00:00.000")
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
