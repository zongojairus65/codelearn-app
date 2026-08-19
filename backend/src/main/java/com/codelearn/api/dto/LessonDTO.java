package com.codelearn.api.dto;

import com.codelearn.api.model.Lesson;

public class LessonDTO {
    private Long id;
    private String title;
    private String theoryContent;
    private Integer orderIndex;
    private Integer xpReward;

    public static LessonDTO fromEntity(Lesson lesson) {
        LessonDTO dto = new LessonDTO();
        dto.id = lesson.getId();
        dto.title = lesson.getTitle();
        dto.theoryContent = lesson.getTheoryContent();
        dto.orderIndex = lesson.getOrderIndex();
        dto.xpReward = lesson.getXpReward();
        return dto;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getTheoryContent() { return theoryContent; }
    public Integer getOrderIndex() { return orderIndex; }
    public Integer getXpReward() { return xpReward; }
}
