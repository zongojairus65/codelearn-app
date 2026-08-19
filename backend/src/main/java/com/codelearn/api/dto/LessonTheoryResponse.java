package com.codelearn.api.dto;

public class LessonTheoryResponse {
    private Long lessonId;
    private String title;
    private String theory;

    public LessonTheoryResponse(Long lessonId, String title, String theory) {
        this.lessonId = lessonId;
        this.title = title;
        this.theory = theory;
    }

    public Long getLessonId() { return lessonId; }
    public String getTitle() { return title; }
    public String getTheory() { return theory; }
}
