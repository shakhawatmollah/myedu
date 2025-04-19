package com.shakhawat.myedu.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Schema(description = "Department Data Transfer Object")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentDTO {
    private String id;

    @NotBlank(message = "Department name is required")
    private String name;

    @NotNull(message = "University ID is required")
    private String universityId;

    private String description;

    private String headOfDepartment;

    private List<String> teacherIds;

    private List<String> studentIds;
}
