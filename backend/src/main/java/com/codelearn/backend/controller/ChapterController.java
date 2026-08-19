package com.codelearn.backend.controller;

import com.codelearn.backend.dto.ChapterDTO;
import com.codelearn.backend.model.Chapter;
import com.codelearn.backend.repository.ChapterRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/chapters")
public class ChapterController {

    private final ChapterRepository chapterRepository;

    public ChapterController(ChapterRepository chapterRepository) {
        this.chapterRepository = chapterRepository;
    }

    @GetMapping
    public List<ChapterDTO> getAllChapters() {
        List<Chapter> chapters = chapterRepository.findAllByOrderByOrderIndexAsc();
        return chapters.stream()
                .map(ChapterDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
