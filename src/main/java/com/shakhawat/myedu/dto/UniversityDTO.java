package com.shakhawat.myedu.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
