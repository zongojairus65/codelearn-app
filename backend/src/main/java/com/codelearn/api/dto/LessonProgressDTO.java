package com.codelearn.api.dto;

public class LessonProgressDTO {
    private Long id;
    private String title;
    private Integer orderIndex;
    private Integer xpReward;
    private String status; // COMPLETED, UNLOCKED, LOCKED

    public LessonProgressDTO(Long id, String title, Integer orderIndex, Integer xpReward, String status) {
        this.id = id;
        this.title = title;
        this.orderIndex = orderIndex;
        this.xpReward = xpReward;
        this.status = status;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public Integer getOrderIndex() { return orderIndex; }
    public Integer getXpReward() { return xpReward; }
    public String getStatus() { return status; }
}
