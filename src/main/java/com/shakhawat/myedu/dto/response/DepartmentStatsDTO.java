package com.shakhawat.myedu.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentStatsDTO {
    private String departmentId;
    private String departmentName;
    private long teacherCount;
    private long studentCount;
    private double averageGrade;
}
