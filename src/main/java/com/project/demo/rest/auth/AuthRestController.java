package com.project.demo.rest.auth;

import com.project.demo.logic.entity.auth.AuthenticationService;
import com.project.demo.logic.entity.auth.JwtService;
import com.project.demo.logic.entity.auth.dto.AuthResponse;
import com.project.demo.logic.entity.auth.dto.LoginRequest;
import com.project.demo.logic.entity.user.User;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RestController
public class AuthRestController {

    private final AuthenticationService authenticationService;
    private final JwtService jwtService;

    public AuthRestController(AuthenticationService authenticationService, JwtService jwtService) {
        this.authenticationService = authenticationService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        User authenticatedUser = authenticationService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());
        String token = jwtService.generateToken(authenticatedUser);

        AuthResponse response = new AuthResponse(
                token,
                jwtService.getExpirationTime(),
                authenticatedUser.getEmail(),
                authenticatedUser.getName(),
                authenticatedUser.getRole().getName().toString()
        );

        return ResponseEntity.ok(response);
    }
}
