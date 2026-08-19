package com.codelearn.backend.dto;

import com.codelearn.backend.model.Chapter;

public class ChapterDTO {
    private Long id;
    private String title;
    private String description;
    private Integer orderIndex;
    private String status;

    public static ChapterDTO fromEntity(Chapter chapter) {
        ChapterDTO dto = new ChapterDTO();
        dto.id = chapter.getId();
        dto.title = chapter.getTitle();
        dto.description = chapter.getDescription();
        dto.orderIndex = chapter.getOrderIndex();
        dto.status = chapter.getStatus().name();
        return dto;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public Integer getOrderIndex() { return orderIndex; }
    public String getStatus() { return status; }
}
