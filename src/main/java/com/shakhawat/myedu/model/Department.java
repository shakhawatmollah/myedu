package com.shakhawat.myedu.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Department entity within a university")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "departments")
@CompoundIndex(name = "univ_dept_idx", def = "{'universityId': 1, 'name': 1}", unique = true)
public class Department {
    @Id
    @Schema(description = "The unique identifier of the department", example = "65a1f8a3e4b0e3a7f8a3e4b1")
    private String id;

    @Schema(description = "Name of the department", example = "Computer Science")
    @NotBlank
    private String name;

    @Schema(description = "ID of the university to which the department belongs", example = "65a1f8a3e4b0e3a7f8a3e4b1")
    @NotNull
    private String universityId;

    @Schema(description = "Description of the department", example = "Department of Computer Science")
    private String description;

    @Schema(description = "Head of the department", example = "Shakhawat Mollah")
    private String headOfDepartment;

    @Schema(description = "List of teachers in this department")
    private List<String> teacherIds;

    @Schema(description = "List of students in this department")
    private List<String> studentIds;

    @Schema(description = "Creation date of the department record", example = "2025-05-01T10:00:00.000")
    @CreatedDate
    private LocalDateTime createdAt;

    @Schema(description = "Last updated date of the department record", example = "2025-05-01T10:00:00.000")
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
