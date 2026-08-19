package com.codelearn.api.controller;

import com.codelearn.api.dto.ExerciseDTO;
import com.codelearn.api.dto.LessonTheoryResponse;
import com.codelearn.api.model.Exercise;
import com.codelearn.api.model.Lesson;
import com.codelearn.api.repository.ExerciseRepository;
import com.codelearn.api.repository.LessonRepository;
import com.codelearn.api.service.GeminiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/lessons")
public class LessonController {

    private final LessonRepository lessonRepository;
    private final ExerciseRepository exerciseRepository;
    private final GeminiService geminiService;

    private static final int MIN_THEORY_LENGTH = 150;

    public LessonController(LessonRepository lessonRepository,
                             ExerciseRepository exerciseRepository,
                             GeminiService geminiService) {
        this.lessonRepository = lessonRepository;
        this.exerciseRepository = exerciseRepository;
        this.geminiService = geminiService;
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

    @GetMapping("/{id}/theory")
    public ResponseEntity<?> getLessonTheory(@PathVariable Long id) {
        Lesson lesson = lessonRepository.findById(id).orElse(null);
        if (lesson == null) {
            return ResponseEntity.notFound().build();
        }

        String existing = lesson.getTheoryContent();
        if (existing != null && existing.trim().length() >= MIN_THEORY_LENGTH) {
            return ResponseEntity.ok(new LessonTheoryResponse(lesson.getId(), lesson.getTitle(), existing));
        }

        String systemPrompt = "Tu es un professeur de programmation qui explique le HTML/CSS/JS de façon "
                + "claire, chaleureuse et progressive à un débutant complet. Réponds uniquement avec le texte "
                + "de l'explication, en français, sans titre markdown, sans introduction du type 'Voici', "
                + "directement le contenu pédagogique. Utilise des paragraphes courts. Longueur : 150 à 300 mots.";

        String userPrompt = "Explique la notion suivante pour la leçon intitulée \"" + lesson.getTitle()
                + "\" du chapitre \"" + lesson.getChapter().getTitle() + "\". "
                + "Contexte de base à mentionner si utile : " + existing;

        try {
            String generated = geminiService.generateContent(systemPrompt, userPrompt);
            lesson.setTheoryContent(generated);
            lessonRepository.save(lesson);
            return ResponseEntity.ok(new LessonTheoryResponse(lesson.getId(), lesson.getTitle(), generated));
        } catch (Exception e) {
            String fallback = (existing != null && !existing.isBlank())
                    ? existing
                    : "Impossible de charger l'explication pour le moment.";
            return ResponseEntity.ok(new LessonTheoryResponse(lesson.getId(), lesson.getTitle(), fallback));
        }
    }
}
