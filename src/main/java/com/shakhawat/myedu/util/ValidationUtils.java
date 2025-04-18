package com.shakhawat.myedu.util;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class ValidationUtils {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^\\+?[0-9]{10,15}$");

    public boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    public boolean isValidPhone(String phone) {
        return phone != null && PHONE_PATTERN.matcher(phone).matches();
    }

    public boolean isValidPassword(String password) {
        // Password should be at least 8 characters, with at least one digit,
        // one lowercase, one uppercase, and one special character
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
        return password != null && password.matches(passwordPattern);
    }

    public boolean isAlphanumeric(String input) {
        return input != null && input.matches("^[a-zA-Z0-9]*$");
    }

    public boolean isNumeric(String input) {
        return input != null && input.matches("^[0-9]*$");
    }
}
