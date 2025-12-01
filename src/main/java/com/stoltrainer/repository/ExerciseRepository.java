package com.stoltrainer.repository;

import com.stoltrainer.model.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    List<Exercise> findByUserIdAndExerciseDate(Long userId, LocalDate exerciseDate);
    List<Exercise> findByUserIdOrderByExerciseDateDesc(Long userId);
    List<Exercise> findByUserIdAndExerciseDateBetween(Long userId, LocalDate startDate, LocalDate endDate);
}

