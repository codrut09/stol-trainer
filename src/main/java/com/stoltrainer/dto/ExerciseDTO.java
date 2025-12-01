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
public class ExerciseDTO {
    private Long id;
    private Long userId;

    @NotBlank(message = "Exercise name is required")
    private String name;

    @NotNull(message = "Sets is required")
    @Positive(message = "Sets must be positive")
    private Integer sets;

    @NotNull(message = "Reps is required")
    @Positive(message = "Reps must be positive")
    private Integer reps;

    @Positive(message = "Weight must be positive")
    private Integer weight;

    private String notes;

    private LocalDate exerciseDate;
}
