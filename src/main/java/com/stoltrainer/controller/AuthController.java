package com.stoltrainer.controller;

import com.stoltrainer.dto.LoginRequest;
import com.stoltrainer.dto.LoginResponse;
import com.stoltrainer.dto.ApiResponse;
import com.stoltrainer.model.User;
import com.stoltrainer.repository.UserRepository;
import com.stoltrainer.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            Optional<User> userOptional = userRepository.findByUsername(loginRequest.getUsername());

            if (userOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse("error", "User not found", null, System.currentTimeMillis()));
            }

            User user = userOptional.get();

            if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse("error", "Invalid password", null, System.currentTimeMillis()));
            }

            if (!user.getActive()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ApiResponse("error", "User account is inactive", null, System.currentTimeMillis()));
            }

            String token = jwtTokenProvider.generateToken(user.getUsername());

            LoginResponse loginResponse = new LoginResponse(
                    token,
                    user.getUsername(),
                    user.getRole().toString(),
                    user.getId()
            );

            return ResponseEntity.ok(new ApiResponse("success", "Login successful", loginResponse, System.currentTimeMillis()));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("error", "Login failed: " + e.getMessage(), null, System.currentTimeMillis()));
        }
    }
}

