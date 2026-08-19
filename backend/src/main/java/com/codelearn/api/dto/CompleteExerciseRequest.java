package com.codelearn.api.dto;

public class CompleteExerciseRequest {
    private Long userId;
    private Long exerciseId;
    private Integer score;

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getExerciseId() { return exerciseId; }
    public void setExerciseId(Long exerciseId) { this.exerciseId = exerciseId; }
    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }
}
