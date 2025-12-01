package com.stoltrainer.controller;

import com.stoltrainer.dto.ExerciseDTO;
import com.stoltrainer.dto.ApiResponse;
import com.stoltrainer.service.ExerciseService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/exercises")
@RequiredArgsConstructor
public class ExerciseController {
    private final ExerciseService exerciseService;

    @PostMapping("/user/{userId}")
    public ResponseEntity<?> createExercise(
            @PathVariable Long userId,
            @RequestBody ExerciseDTO exerciseDTO) {
        try {
            if (exerciseDTO.getName() == null || exerciseDTO.getName().isBlank()) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse("error", "Exercise name is required", null, System.currentTimeMillis()));
            }
            if (exerciseDTO.getExerciseDate() == null) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse("error", "Exercise date is required", null, System.currentTimeMillis()));
            }

            ExerciseDTO createdExercise = exerciseService.createExercise(userId, exerciseDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse("success", "Exercise created successfully", createdExercise, System.currentTimeMillis()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse("error", e.getMessage(), null, System.currentTimeMillis()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("error", "Failed to create exercise: " + e.getMessage(), null, System.currentTimeMillis()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getExerciseById(@PathVariable Long id) {
        try {
            ExerciseDTO exerciseDTO = exerciseService.getExerciseById(id);
            return ResponseEntity.ok(new ApiResponse("success", "Exercise found", exerciseDTO, System.currentTimeMillis()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("error", e.getMessage(), null, System.currentTimeMillis()));
        }
    }

    @GetMapping("/user/{userId}/date")
    public ResponseEntity<?> getExercisesByUserAndDate(
            @PathVariable Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        try {
            List<ExerciseDTO> exercises = exerciseService.getExercisesByUserAndDate(userId, date);
            return ResponseEntity.ok(new ApiResponse("success", "Exercises retrieved", exercises, System.currentTimeMillis()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("error", "Failed to retrieve exercises: " + e.getMessage(), null, System.currentTimeMillis()));
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getExercisesByUser(@PathVariable Long userId) {
        try {
            List<ExerciseDTO> exercises = exerciseService.getExercisesByUser(userId);
            return ResponseEntity.ok(new ApiResponse("success", "Exercises retrieved", exercises, System.currentTimeMillis()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("error", "Failed to retrieve exercises: " + e.getMessage(), null, System.currentTimeMillis()));
        }
    }

    @GetMapping("/user/{userId}/range")
    public ResponseEntity<?> getExercisesByDateRange(
            @PathVariable Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            if (startDate.isAfter(endDate)) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse("error", "Start date must be before end date", null, System.currentTimeMillis()));
            }
            List<ExerciseDTO> exercises = exerciseService.getExercisesByUserAndDateRange(userId, startDate, endDate);
            return ResponseEntity.ok(new ApiResponse("success", "Exercises retrieved", exercises, System.currentTimeMillis()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("error", "Failed to retrieve exercises: " + e.getMessage(), null, System.currentTimeMillis()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateExercise(
            @PathVariable Long id,
            @RequestBody ExerciseDTO exerciseDTO) {
        try {
            if (exerciseDTO.getName() == null || exerciseDTO.getName().isBlank()) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse("error", "Exercise name is required", null, System.currentTimeMillis()));
            }
            ExerciseDTO updatedExercise = exerciseService.updateExercise(id, exerciseDTO);
            return ResponseEntity.ok(new ApiResponse("success", "Exercise updated successfully", updatedExercise, System.currentTimeMillis()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("error", e.getMessage(), null, System.currentTimeMillis()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteExercise(@PathVariable Long id) {
        try {
            exerciseService.deleteExercise(id);
            return ResponseEntity.ok(new ApiResponse("success", "Exercise deleted successfully", null, System.currentTimeMillis()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("error", e.getMessage(), null, System.currentTimeMillis()));
        }
    }
}
