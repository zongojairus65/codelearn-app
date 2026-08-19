package com.codelearn.api.controller;

import com.codelearn.api.dto.ExerciseDTO;
import com.codelearn.api.model.Exercise;
import com.codelearn.api.repository.ExerciseRepository;
import com.codelearn.api.repository.LessonRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/lessons")
public class LessonController {

    private final LessonRepository lessonRepository;
    private final ExerciseRepository exerciseRepository;

    public LessonController(LessonRepository lessonRepository, ExerciseRepository exerciseRepository) {
        this.lessonRepository = lessonRepository;
        this.exerciseRepository = exerciseRepository;
    }

    @GetMapping("/{id}/exercises")
    public ResponseEntity<List<ExerciseDTO>> getExercisesByLesson(@PathVariable Long id) {
        if (!lessonRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        List<Exercise> exercises = exerciseRepository.findByLessonIdOrderByOrderIndexAsc(id);
        List<ExerciseDTO> dtos = exercises.stream()
                .map(ExerciseDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
}
