package com.codelearn.api.controller;

import com.codelearn.api.dto.ChapterDTO;
import com.codelearn.api.dto.LessonDTO;
import com.codelearn.api.model.Chapter;
import com.codelearn.api.model.Lesson;
import com.codelearn.api.repository.ChapterRepository;
import com.codelearn.api.repository.LessonRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/chapters")
public class ChapterController {

    private final ChapterRepository chapterRepository;
    private final LessonRepository lessonRepository;

    public ChapterController(ChapterRepository chapterRepository, LessonRepository lessonRepository) {
        this.chapterRepository = chapterRepository;
        this.lessonRepository = lessonRepository;
    }

    @GetMapping
    public List<ChapterDTO> getAllChapters() {
        List<Chapter> chapters = chapterRepository.findAllByOrderByOrderIndexAsc();
        return chapters.stream()
                .map(ChapterDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}/lessons")
    public ResponseEntity<List<LessonDTO>> getLessonsByChapter(@PathVariable Long id) {
        if (!chapterRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        List<Lesson> lessons = lessonRepository.findByChapterIdOrderByOrderIndexAsc(id);
        List<LessonDTO> dtos = lessons.stream()
                .map(LessonDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
}
