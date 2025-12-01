package com.stoltrainer.service;

import com.stoltrainer.dto.MealDTO;
import com.stoltrainer.model.Meal;
import com.stoltrainer.model.User;
import com.stoltrainer.repository.MealRepository;
import com.stoltrainer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MealService {
    private final MealRepository mealRepository;
    private final UserRepository userRepository;

    public MealDTO createMeal(Long userId, MealDTO mealDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Meal meal = new Meal();
        meal.setUser(user);
        meal.setName(mealDTO.getName());
        meal.setCalories(mealDTO.getCalories());
        meal.setProtein(mealDTO.getProtein());
        meal.setCarbs(mealDTO.getCarbs());
        meal.setFats(mealDTO.getFats());
        meal.setNotes(mealDTO.getNotes());
        meal.setMealDate(mealDTO.getMealDate() != null ? mealDTO.getMealDate() : LocalDate.now());

        Meal savedMeal = mealRepository.save(meal);
        return convertToDTO(savedMeal);
    }

    public MealDTO getMealById(Long id) {
        Meal meal = mealRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Meal not found"));
        return convertToDTO(meal);
    }

    public List<MealDTO> getMealsByUserAndDate(Long userId, LocalDate date) {
        return mealRepository.findByUserIdAndMealDate(userId, date).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<MealDTO> getMealsByUser(Long userId) {
        return mealRepository.findByUserIdOrderByMealDateDesc(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<MealDTO> getMealsByUserAndDateRange(Long userId, LocalDate startDate, LocalDate endDate) {
        return mealRepository.findByUserIdAndMealDateBetween(userId, startDate, endDate).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public MealDTO updateMeal(Long id, MealDTO mealDTO) {
        Meal meal = mealRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Meal not found"));

        meal.setName(mealDTO.getName());
        meal.setCalories(mealDTO.getCalories());
        meal.setProtein(mealDTO.getProtein());
        meal.setCarbs(mealDTO.getCarbs());
        meal.setFats(mealDTO.getFats());
        meal.setNotes(mealDTO.getNotes());
        if (mealDTO.getMealDate() != null) {
            meal.setMealDate(mealDTO.getMealDate());
        }

        Meal updatedMeal = mealRepository.save(meal);
        return convertToDTO(updatedMeal);
    }

    public void deleteMeal(Long id) {
        mealRepository.deleteById(id);
    }

    private MealDTO convertToDTO(Meal meal) {
        return new MealDTO(
                meal.getId(),
                meal.getUser().getId(),
                meal.getName(),
                meal.getCalories(),
                meal.getProtein(),
                meal.getCarbs(),
                meal.getFats(),
                meal.getNotes(),
                meal.getMealDate()
        );
    }
}

