package com.stoltrainer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MealDTO {
    private Long id;
    private Long userId;

    @NotBlank(message = "Meal name is required")
    private String name;

    @NotNull(message = "Calories is required")
    @Positive(message = "Calories must be positive")
    private Integer calories;

    private String protein;
    private String carbs;
    private String fats;
    private String notes;

    private LocalDate mealDate;
}
