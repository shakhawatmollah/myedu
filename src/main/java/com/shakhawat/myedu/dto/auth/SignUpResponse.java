package com.shakhawat.myedu.dto.auth;

import com.shakhawat.myedu.model.Role;

import java.util.Set;

public record SignUpResponse(
        String id,
        String username,
        String email,
        Set<Role> roles
) {}
