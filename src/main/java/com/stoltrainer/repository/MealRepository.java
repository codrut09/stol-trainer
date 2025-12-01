package com.stoltrainer.repository;

import com.stoltrainer.model.Meal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MealRepository extends JpaRepository<Meal, Long> {
    List<Meal> findByUserIdAndMealDate(Long userId, LocalDate mealDate);
    List<Meal> findByUserIdOrderByMealDateDesc(Long userId);
    List<Meal> findByUserIdAndMealDateBetween(Long userId, LocalDate startDate, LocalDate endDate);
}

