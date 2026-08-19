package com.codelearn.api.dto;

public class CompleteExerciseResponse {
    private Integer xpEarned;
    private Integer totalXp;
    private Integer level;
    private Integer currentStreak;
    private Long nextLessonId;
    private boolean chapterCompleted;

    public CompleteExerciseResponse(Integer xpEarned, Integer totalXp, Integer level,
                                     Integer currentStreak, Long nextLessonId, boolean chapterCompleted) {
        this.xpEarned = xpEarned;
        this.totalXp = totalXp;
        this.level = level;
        this.currentStreak = currentStreak;
        this.nextLessonId = nextLessonId;
        this.chapterCompleted = chapterCompleted;
    }

    public Integer getXpEarned() { return xpEarned; }
    public Integer getTotalXp() { return totalXp; }
    public Integer getLevel() { return level; }
    public Integer getCurrentStreak() { return currentStreak; }
    public Long getNextLessonId() { return nextLessonId; }
    public boolean isChapterCompleted() { return chapterCompleted; }
}
