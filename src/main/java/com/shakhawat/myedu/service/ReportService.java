package com.shakhawat.myedu.service;

import com.shakhawat.myedu.dto.response.DepartmentStatsDTO;
import com.shakhawat.myedu.dto.response.ReportResponseDTO;
import com.shakhawat.myedu.model.Department;
import com.shakhawat.myedu.model.Student;
import com.shakhawat.myedu.model.Teacher;
import com.shakhawat.myedu.model.University;
import com.shakhawat.myedu.repository.DepartmentRepository;
import com.shakhawat.myedu.repository.StudentRepository;
import com.shakhawat.myedu.repository.TeacherRepository;
import com.shakhawat.myedu.repository.UniversityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringEscapeUtils.escapeCsv;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final UniversityRepository universityRepository;
    private final DepartmentRepository departmentRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;

    @Transactional(readOnly = true)
    public ReportResponseDTO generateUniversityStats() {
        long totalUniversities = universityRepository.count();
        long totalDepartments = departmentRepository.count();
        long totalTeachers = teacherRepository.count();
        long totalStudents = studentRepository.count();

        Map<String, Object> data = new HashMap<>();
        data.put("totalUniversities", totalUniversities);
        data.put("totalDepartments", totalDepartments);
        data.put("totalTeachers", totalTeachers);
        data.put("totalStudents", totalStudents);

        return ReportResponseDTO.builder()
                .reportName("University Statistics")
                .description("Overall statistics of universities, departments, teachers, and students")
                .data(Collections.singletonList(data))
                .build();
    }

    @Transactional(readOnly = true)
    public ReportResponseDTO generateDepartmentStats(String universityId) {
        List<Department> departments = departmentRepository.findByUniversityId(universityId);
        University university = universityRepository.findById(universityId).orElse(null);

        List<DepartmentStatsDTO> departmentStats = departments.stream()
                .map(department -> {
                    long teacherCount = teacherRepository.countByDepartmentId(department.getId());
                    long studentCount = studentRepository.countByDepartmentId(department.getId());
                    double averageGrade = calculateAverageGradeForDepartment(department.getId());

                    return DepartmentStatsDTO.builder()
                            .departmentId(department.getId())
                            .departmentName(department.getName())
                            .teacherCount(teacherCount)
                            .studentCount(studentCount)
                            .averageGrade(averageGrade)
                            .build();
                })
                .collect(Collectors.toList());

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("universityId", universityId);
        parameters.put("universityName", university != null ? university.getName() : "Unknown");

        return ReportResponseDTO.builder()
                .reportName("Department Statistics")
                .description("Statistics by department for university: " +
                        (university != null ? university.getName() : "Unknown"))
                .parameters(parameters)
                .data(new ArrayList<>(departmentStats))
                .build();
    }

    @Transactional(readOnly = true)
    public ReportResponseDTO generateTeacherDistribution() {
        List<Teacher> teachers = teacherRepository.findAll();

        // Group teachers by qualification
        Map<String, Long> qualificationDistribution = teachers.stream()
                .collect(Collectors.groupingBy(Teacher::getQualification, Collectors.counting()));

        // Group teachers by join date year
        Map<Integer, Long> joinYearDistribution = teachers.stream()
                .collect(Collectors.groupingBy(
                        teacher -> teacher.getJoinDate().getYear(),
                        Collectors.counting()));

        List<Map<String, Object>> result = new ArrayList<>();

        Map<String, Object> qualificationData = new HashMap<>();
        qualificationData.put("distributionType", "Qualification");
        qualificationData.put("distribution", qualificationDistribution);
        result.add(qualificationData);

        Map<String, Object> joinYearData = new HashMap<>();
        joinYearData.put("distributionType", "Join Year");
        joinYearData.put("distribution", joinYearDistribution);
        result.add(joinYearData);

        return ReportResponseDTO.builder()
                .reportName("Teacher Distribution")
                .description("Distribution of teachers by qualification and join year")
                .data(result)
                .build();
    }

    @Transactional(readOnly = true)
    public ReportResponseDTO generateStudentPerformanceReport(String departmentId) {
        Department department = departmentRepository.findById(departmentId).orElse(null);
        List<Student> students = studentRepository.findByDepartmentId(departmentId);

        // Calculate average GPA for each student
        List<Map<String, Object>> studentPerformance = students.stream()
                .map(student -> {
                    double averageGrade = student.getGrades() != null && !student.getGrades().isEmpty() ?
                            student.getGrades().values().stream().mapToDouble(Double::doubleValue).average().orElse(0.0) : 0.0;

                    Map<String, Object> performance = new HashMap<>();
                    performance.put("studentId", student.getStudentId());
                    performance.put("studentName", student.getFirstName() + " " + student.getLastName());
                    performance.put("averageGrade", averageGrade);
                    performance.put("grades", student.getGrades());
                    performance.put("academicYear", student.getAcademicYear());

                    return performance;
                })
                .collect(Collectors.toList());

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("departmentId", departmentId);
        parameters.put("departmentName", department != null ? department.getName() : "Unknown");
        parameters.put("studentCount", students.size());

        return ReportResponseDTO.builder()
                .reportName("Student Performance Report")
                .description("Performance report of students in department: " +
                        (department != null ? department.getName() : "Unknown"))
                .parameters(parameters)
                .data(studentPerformance)
                .build();
    }

    @Transactional(readOnly = true)
    public ReportResponseDTO generateYearlyEnrollmentReport() {
        List<Student> students = studentRepository.findAll();

        // Group students by enrollment year
        Map<Integer, Long> enrollmentByYear = students.stream()
                .collect(Collectors.groupingBy(
                        student -> student.getEnrollmentDate().getYear(),
                        Collectors.counting()));

        // Get trend for the last 5 years
        int currentYear = LocalDate.now().getYear();
        List<Map<String, Object>> yearlyTrend = new ArrayList<>();

        for (int year = currentYear - 4; year <= currentYear; year++) {
            Map<String, Object> yearData = new HashMap<>();
            yearData.put("year", year);
            yearData.put("enrollmentCount", enrollmentByYear.getOrDefault(year, 0L));
            yearlyTrend.add(yearData);
        }

        return ReportResponseDTO.builder()
                .reportName("Yearly Enrollment Report")
                .description("Student enrollment trends over the last 5 years")
                .data(yearlyTrend)
                .build();
    }

    private double calculateAverageGradeForDepartment(String departmentId) {
        List<Student> students = studentRepository.findGradesByDepartmentId(departmentId);

        double totalGrades = 0;
        int gradeCount = 0;

        for (Student student : students) {
            if (student.getGrades() != null && !student.getGrades().isEmpty()) {
                for (Double grade : student.getGrades().values()) {
                    totalGrades += grade;
                    gradeCount++;
                }
            }
        }

        return gradeCount > 0 ? totalGrades / gradeCount : 0;
    }

    public Map<String, Double> getStudentTeacherRatio() {
        List<Department> departments = departmentRepository.findAll();
        Map<String, Double> ratios = new HashMap<>();

        for (Department department : departments) {
            int studentCount = department.getStudentIds().size();
            int teacherCount = department.getTeacherIds().size();
            double ratio = teacherCount > 0 ? (double) studentCount / teacherCount : 0;
            ratios.put(department.getName(), ratio);
        }

        return ratios;
    }

}
