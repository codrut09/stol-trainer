package com.stoltrainer.controller;

import com.stoltrainer.dto.MealDTO;
import com.stoltrainer.dto.ApiResponse;
import com.stoltrainer.service.MealService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/meals")
@RequiredArgsConstructor
public class MealController {
    private final MealService mealService;

    @PostMapping("/user/{userId}")
    public ResponseEntity<?> createMeal(
            @PathVariable Long userId,
            @RequestBody MealDTO mealDTO) {
        try {
            if (mealDTO.getName() == null || mealDTO.getName().isBlank()) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse("error", "Meal name is required", null, System.currentTimeMillis()));
            }
            if (mealDTO.getMealDate() == null) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse("error", "Meal date is required", null, System.currentTimeMillis()));
            }

            MealDTO createdMeal = mealService.createMeal(userId, mealDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse("success", "Meal created successfully", createdMeal, System.currentTimeMillis()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse("error", e.getMessage(), null, System.currentTimeMillis()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("error", "Failed to create meal: " + e.getMessage(), null, System.currentTimeMillis()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMealById(@PathVariable Long id) {
        try {
            MealDTO mealDTO = mealService.getMealById(id);
            return ResponseEntity.ok(new ApiResponse("success", "Meal found", mealDTO, System.currentTimeMillis()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("error", e.getMessage(), null, System.currentTimeMillis()));
        }
    }

    @GetMapping("/user/{userId}/date")
    public ResponseEntity<?> getMealsByUserAndDate(
            @PathVariable Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        try {
            List<MealDTO> meals = mealService.getMealsByUserAndDate(userId, date);
            return ResponseEntity.ok(new ApiResponse("success", "Meals retrieved", meals, System.currentTimeMillis()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("error", "Failed to retrieve meals: " + e.getMessage(), null, System.currentTimeMillis()));
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getMealsByUser(@PathVariable Long userId) {
        try {
            List<MealDTO> meals = mealService.getMealsByUser(userId);
            return ResponseEntity.ok(new ApiResponse("success", "Meals retrieved", meals, System.currentTimeMillis()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("error", "Failed to retrieve meals: " + e.getMessage(), null, System.currentTimeMillis()));
        }
    }

    @GetMapping("/user/{userId}/range")
    public ResponseEntity<?> getMealsByDateRange(
            @PathVariable Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            if (startDate.isAfter(endDate)) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse("error", "Start date must be before end date", null, System.currentTimeMillis()));
            }
            List<MealDTO> meals = mealService.getMealsByUserAndDateRange(userId, startDate, endDate);
            return ResponseEntity.ok(new ApiResponse("success", "Meals retrieved", meals, System.currentTimeMillis()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("error", "Failed to retrieve meals: " + e.getMessage(), null, System.currentTimeMillis()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMeal(
            @PathVariable Long id,
            @RequestBody MealDTO mealDTO) {
        try {
            if (mealDTO.getName() == null || mealDTO.getName().isBlank()) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse("error", "Meal name is required", null, System.currentTimeMillis()));
            }
            MealDTO updatedMeal = mealService.updateMeal(id, mealDTO);
            return ResponseEntity.ok(new ApiResponse("success", "Meal updated successfully", updatedMeal, System.currentTimeMillis()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("error", e.getMessage(), null, System.currentTimeMillis()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMeal(@PathVariable Long id) {
        try {
            mealService.deleteMeal(id);
            return ResponseEntity.ok(new ApiResponse("success", "Meal deleted successfully", null, System.currentTimeMillis()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("error", e.getMessage(), null, System.currentTimeMillis()));
        }
    }
}
