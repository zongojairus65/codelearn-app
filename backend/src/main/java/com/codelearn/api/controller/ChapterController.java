package com.codelearn.api.controller;

import com.codelearn.api.dto.ChapterDTO;
import com.codelearn.api.model.Chapter;
import com.codelearn.api.repository.ChapterRepository;
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
