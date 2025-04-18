package com.shakhawat.myedu.service;

import com.shakhawat.myedu.dto.auth.LoginRequestDTO;
import com.shakhawat.myedu.dto.auth.LoginResponseDTO;
import com.shakhawat.myedu.dto.auth.SignUpRequestDTO;
import com.shakhawat.myedu.exception.EmailAlreadyExistsException;
import com.shakhawat.myedu.exception.UsernameAlreadyExistsException;
import com.shakhawat.myedu.model.Role;
import com.shakhawat.myedu.model.User;
import com.shakhawat.myedu.repository.UserRepository;
import com.shakhawat.myedu.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public LoginResponseDTO login(LoginRequestDTO loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenProvider.createToken(authentication);

        org.springframework.security.core.userdetails.UserDetails userDetails =
                (org.springframework.security.core.userdetails.UserDetails) authentication.getPrincipal();

        Set<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        return LoginResponseDTO.builder()
                .token(jwt)
                .username(userDetails.getUsername())
                .roles(roles)
                .build();
    }

    public User signUp(SignUpRequestDTO signUpDto) {
        // Validate username uniqueness
        if (userRepository.existsByUsername(signUpDto.getUsername())) {
            throw new UsernameAlreadyExistsException("Username is already taken!");
        }

        // Validate email uniqueness
        if (userRepository.existsByEmail(signUpDto.getEmail())) {
            throw new EmailAlreadyExistsException("Email is already in use!");
        }

        // Create new user
        User user = new User();
        user.setUsername(signUpDto.getUsername());
        user.setEmail(signUpDto.getEmail());
        user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));

        Set<Role> roles = signUpDto.getRoles();
        if (roles == null || roles.isEmpty()) {
            roles = Set.of(Role.ROLE_STUDENT);
        }
        user.setRoles(roles);

        return userRepository.save(user);
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("User not found"));
    }

    public void logout() {
        SecurityContextHolder.clearContext();
    }
}
