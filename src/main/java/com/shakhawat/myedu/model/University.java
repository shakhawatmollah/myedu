package com.shakhawat.myedu.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "University entity representing an educational institution")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "universities")
public class University {

    @Id
    @Schema(description = "The unique identifier of the university", example = "680295177608567b86f514b3")
    private String id;

    @Schema(description = "Name of the university", example = "National University")
    @NotBlank
    @Indexed(unique = true)
    private String name;

    @Schema(description = "Location of the university", example = "Dhaka, Bangladesh")
    @NotBlank
    private String address;

    @Schema(description = "Website of the university", example = "https://example.com")
    private String website;

    @Schema(description = "Description of the university", example = "National University of Bangladesh")
    private String description;

    @Schema(description = "Year of establishment", example = "1999")
    private String foundedYear;

    @Schema(description = "List of departments in the university")
    private List<String> departmentIds;

    @Schema(description = "Creation date of the university record", example = "2025-08-01T10:00:00.000")
    @CreatedDate
    private LocalDateTime createdAt;

    @Schema(description = "Last updated date of the university record", example = "2025-08-01T10:00:00.000")
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
