package com.stoltrainer.service;

import com.stoltrainer.dto.ExerciseDTO;
import com.stoltrainer.model.Exercise;
import com.stoltrainer.model.User;
import com.stoltrainer.repository.ExerciseRepository;
import com.stoltrainer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExerciseService {
    private final ExerciseRepository exerciseRepository;
    private final UserRepository userRepository;

    public ExerciseDTO createExercise(Long userId, ExerciseDTO exerciseDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Exercise exercise = new Exercise();
        exercise.setUser(user);
        exercise.setName(exerciseDTO.getName());
        exercise.setSets(exerciseDTO.getSets());
        exercise.setReps(exerciseDTO.getReps());
        exercise.setWeight(exerciseDTO.getWeight());
        exercise.setNotes(exerciseDTO.getNotes());
        exercise.setExerciseDate(exerciseDTO.getExerciseDate() != null ? exerciseDTO.getExerciseDate() : LocalDate.now());

        Exercise savedExercise = exerciseRepository.save(exercise);
        return convertToDTO(savedExercise);
    }

    public ExerciseDTO getExerciseById(Long id) {
        Exercise exercise = exerciseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Exercise not found"));
        return convertToDTO(exercise);
    }

    public List<ExerciseDTO> getExercisesByUserAndDate(Long userId, LocalDate date) {
        return exerciseRepository.findByUserIdAndExerciseDate(userId, date).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ExerciseDTO> getExercisesByUser(Long userId) {
        return exerciseRepository.findByUserIdOrderByExerciseDateDesc(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ExerciseDTO> getExercisesByUserAndDateRange(Long userId, LocalDate startDate, LocalDate endDate) {
        return exerciseRepository.findByUserIdAndExerciseDateBetween(userId, startDate, endDate).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ExerciseDTO updateExercise(Long id, ExerciseDTO exerciseDTO) {
        Exercise exercise = exerciseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Exercise not found"));

        exercise.setName(exerciseDTO.getName());
        exercise.setSets(exerciseDTO.getSets());
        exercise.setReps(exerciseDTO.getReps());
        exercise.setWeight(exerciseDTO.getWeight());
        exercise.setNotes(exerciseDTO.getNotes());
        if (exerciseDTO.getExerciseDate() != null) {
            exercise.setExerciseDate(exerciseDTO.getExerciseDate());
        }

        Exercise updatedExercise = exerciseRepository.save(exercise);
        return convertToDTO(updatedExercise);
    }

    public void deleteExercise(Long id) {
        exerciseRepository.deleteById(id);
    }

    private ExerciseDTO convertToDTO(Exercise exercise) {
        return new ExerciseDTO(
                exercise.getId(),
                exercise.getUser().getId(),
                exercise.getName(),
                exercise.getSets(),
                exercise.getReps(),
                exercise.getWeight(),
                exercise.getNotes(),
                exercise.getExerciseDate()
        );
    }
}

