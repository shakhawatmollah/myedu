package com.shakhawat.myedu.controller;

import com.shakhawat.myedu.dto.auth.LoginRequestDTO;
import com.shakhawat.myedu.dto.auth.LoginResponseDTO;
import com.shakhawat.myedu.dto.auth.SignUpRequestDTO;
import com.shakhawat.myedu.dto.auth.SignUpResponse;
import com.shakhawat.myedu.dto.response.ErrorResponse;
import com.shakhawat.myedu.exception.EmailAlreadyExistsException;
import com.shakhawat.myedu.exception.UsernameAlreadyExistsException;
import com.shakhawat.myedu.model.User;
import com.shakhawat.myedu.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();

    @PostMapping("/login")
    @Operation(summary = "Login user and generate JWT token")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO authRequest) {
        return ResponseEntity.ok(authService.login(authRequest));
    }

    @GetMapping("/me")
    @Operation(summary = "Get current user info", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<User> getCurrentUser() {
        return ResponseEntity.ok(authService.getCurrentUser());
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout user", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> logout(HttpServletRequest request,
                                       HttpServletResponse response,
                                       Authentication authentication) {
        authService.logout();
        logoutHandler.logout(request, response, authentication);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequestDTO signUpDto) {
        try {
            User user = authService.signUp(signUpDto);

            return ResponseEntity.status(HttpStatus.CREATED).body(
                    new SignUpResponse(
                            user.getId(),
                            user.getUsername(),
                            user.getEmail(),
                            user.getRoles()
                    )
            );
        } catch (UsernameAlreadyExistsException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse("username", ex.getMessage()));
        } catch (EmailAlreadyExistsException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse("email", ex.getMessage()));
        }
    }

}
