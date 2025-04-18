package com.shakhawat.myedu.controller;

import com.shakhawat.myedu.dto.response.ReportResponseDTO;
import com.shakhawat.myedu.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@Tag(name = "Reports", description = "University reports APIs")
@SecurityRequirement(name = "bearerAuth")
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/university/summary")
    @Operation(summary = "Get university summary report")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ReportResponseDTO> getUniversitySummaryReport() {
        return ResponseEntity.ok(reportService.generateUniversityStats());
    }

    @GetMapping("/department/statistics")
    @Operation(summary = "Get department statistics report")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ReportResponseDTO> getDepartmentStatisticsReport(String universityId) {
        return ResponseEntity.ok(reportService.generateDepartmentStats(universityId));
    }

    @GetMapping("/teacher/distribution")
    @Operation(summary = "Get Teacher Distribution report")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ReportResponseDTO> getStudentDemographicsReport() {
        return ResponseEntity.ok(reportService.generateTeacherDistribution());
    }

    @GetMapping("/student/performance")
    @Operation(summary = "Get Student Performance report")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ReportResponseDTO> generateStudentPerformanceReport(String universityId) {
        return ResponseEntity.ok(reportService.generateStudentPerformanceReport(universityId));
    }

    @GetMapping("/enrollment/trend")
    @Operation(summary = "Get enrollment trends over time")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ReportResponseDTO> generateYearlyEnrollmentReport() {
        return ResponseEntity.ok(reportService.generateYearlyEnrollmentReport());
    }

    @GetMapping("/department/student-teacher-ratio")
    @Operation(summary = "Get student-teacher ratio by department")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Double>> getStudentTeacherRatioReport() {
        return ResponseEntity.ok(reportService.getStudentTeacherRatio());
    }

}
