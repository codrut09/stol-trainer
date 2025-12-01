package com.stoltrainer.controller;

import com.stoltrainer.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity<ApiResponse<Map<String, String>>> health() {
        Map<String, String> data = new HashMap<>();
        data.put("service", "Fitness Trainer Backend");
        data.put("status", "UP");
        data.put("version", "1.0.0-MVP");

        ApiResponse<Map<String, String>> response = new ApiResponse<>(
                "success",
                "Service is healthy",
                data,
                System.currentTimeMillis()
        );
        return ResponseEntity.ok(response);
    }
}
