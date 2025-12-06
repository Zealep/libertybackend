package com.zealepsoluciones.libertybackend.controller;

import com.zealepsoluciones.libertybackend.config.JwtServiceImpl;
import com.zealepsoluciones.libertybackend.model.dto.AuthRequest;
import com.zealepsoluciones.libertybackend.model.dto.AuthResponse;
import com.zealepsoluciones.libertybackend.model.entity.User;
import com.zealepsoluciones.libertybackend.service.impl.AuthServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtServiceImpl jwtService;
    private final AuthServiceImpl userDetailsService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
        } catch (AuthenticationException e) {
            throw new RuntimeException("Invalid credentials");
        }
        var user = (User) userDetailsService.loadUserByUsername(request.getUsername());
        String token = jwtService.generateToken(user);

        AuthResponse response = new AuthResponse(token, user.getId() ,user.getUsername());
        return ResponseEntity.ok(response);

    }
}
