package com.shakhawat.myedu.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Schema(description = "University Data Transfer Object")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UniversityDTO {
    private String id;

    @NotBlank(message = "University name is required")
    private String name;

    @NotBlank(message = "University address is required")
    private String address;

    private String website;

    private String description;

    private String foundedYear;

    private List<String> departmentIds;
}
