package com.codelearn.api.controller;

import com.codelearn.api.dto.CompleteExerciseRequest;
import com.codelearn.api.dto.CompleteExerciseResponse;
import com.codelearn.api.dto.LessonProgressDTO;
import com.codelearn.api.service.ProgressService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/progress")
public class ProgressController {

    private final ProgressService progressService;

    public ProgressController(ProgressService progressService) {
        this.progressService = progressService;
    }

    @PostMapping("/complete")
    public ResponseEntity<?> completeExercise(@RequestBody CompleteExerciseRequest request) {
        try {
            CompleteExerciseResponse response = progressService.completeExercise(
                    request.getUserId(), request.getExerciseId(), request.getScore());
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/chapters/{chapterId}")
    public ResponseEntity<List<LessonProgressDTO>> getChapterProgress(
            @PathVariable Long chapterId, @RequestParam Long userId) {
        return ResponseEntity.ok(progressService.getChapterProgress(userId, chapterId));
    }
}
