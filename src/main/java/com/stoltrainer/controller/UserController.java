package com.stoltrainer.controller;

import com.stoltrainer.dto.CreateUserRequest;
import com.stoltrainer.dto.UserDTO;
import com.stoltrainer.dto.ApiResponse;
import com.stoltrainer.model.UserRole;
import com.stoltrainer.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody CreateUserRequest request) {
        try {
            if (request.getUsername() == null || request.getUsername().isBlank()) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse("error", "Username is required", null, System.currentTimeMillis()));
            }
            if (request.getPassword() == null || request.getPassword().isBlank()) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse("error", "Password is required", null, System.currentTimeMillis()));
            }
            if (request.getEmail() == null || request.getEmail().isBlank()) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse("error", "Email is required", null, System.currentTimeMillis()));
            }

            UserDTO userDTO = userService.createUser(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse("success", "User registered successfully", userDTO, System.currentTimeMillis()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse("error", e.getMessage(), null, System.currentTimeMillis()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("error", "Registration failed: " + e.getMessage(), null, System.currentTimeMillis()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        try {
            UserDTO userDTO = userService.getUserById(id);
            return ResponseEntity.ok(new ApiResponse("success", "User found", userDTO, System.currentTimeMillis()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("error", e.getMessage(), null, System.currentTimeMillis()));
        }
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<?> getUserByUsername(@PathVariable String username) {
        try {
            UserDTO userDTO = userService.getUserByUsername(username);
            return ResponseEntity.ok(new ApiResponse("success", "User found", userDTO, System.currentTimeMillis()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("error", e.getMessage(), null, System.currentTimeMillis()));
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        try {
            List<UserDTO> users = userService.getAllUsers();
            return ResponseEntity.ok(new ApiResponse("success", "Users retrieved", users, System.currentTimeMillis()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("error", "Failed to retrieve users", null, System.currentTimeMillis()));
        }
    }

    @GetMapping("/role/{role}")
    public ResponseEntity<?> getUsersByRole(@PathVariable String role) {
        try {
            UserRole userRole = UserRole.valueOf(role.toUpperCase());
            List<UserDTO> users = userService.getUsersByRole(userRole);
            return ResponseEntity.ok(new ApiResponse("success", "Users retrieved", users, System.currentTimeMillis()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse("error", "Invalid role: " + role, null, System.currentTimeMillis()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody CreateUserRequest request) {
        try {
            UserDTO userDTO = userService.updateUser(id, request);
            return ResponseEntity.ok(new ApiResponse("success", "User updated successfully", userDTO, System.currentTimeMillis()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("error", e.getMessage(), null, System.currentTimeMillis()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok(new ApiResponse("success", "User deleted successfully", null, System.currentTimeMillis()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("error", e.getMessage(), null, System.currentTimeMillis()));
        }
    }
}
