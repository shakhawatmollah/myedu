package com.shakhawat.myedu.model;

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

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "departments")
@CompoundIndex(name = "univ_dept_idx", def = "{'universityId': 1, 'name': 1}", unique = true)
public class Department {
    @Id
    private String id;

    @NotBlank
    private String name;

    @NotNull
    private String universityId;

    private String description;

    private String headOfDepartment;

    private List<String> teacherIds;

    private List<String> studentIds;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
