package com.shakhawat.myedu.util;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class DateUtils {

    private static final DateTimeFormatter DEFAULT_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DEFAULT_DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public String formatDate(LocalDate date) {
        return date != null ? date.format(DEFAULT_DATE_FORMATTER) : null;
    }

    public String formatDateTime(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(DEFAULT_DATETIME_FORMATTER) : null;
    }

    public LocalDate parseDate(String dateStr) {
        return dateStr != null ? LocalDate.parse(dateStr, DEFAULT_DATE_FORMATTER) : null;
    }

    public int calculateAge(LocalDate birthDate) {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    public List<String> getLastNYears(int n) {
        int currentYear = LocalDate.now().getYear();
        return IntStream.rangeClosed(currentYear - n + 1, currentYear)
                .mapToObj(String::valueOf)
                .collect(Collectors.toList());
    }

    public boolean isDateInPast(LocalDate date) {
        return date.isBefore(LocalDate.now());
    }

    public String getCurrentAcademicYear() {
        int currentYear = LocalDate.now().getYear();
        int month = LocalDate.now().getMonthValue();

        // Academic year typically starts in September (month 9)
        if (month >= 9) {
            return currentYear + "-" + (currentYear + 1);
        } else {
            return (currentYear - 1) + "-" + currentYear;
        }
    }
}
