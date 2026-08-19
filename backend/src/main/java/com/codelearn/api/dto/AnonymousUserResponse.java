package com.codelearn.api.dto;

public class AnonymousUserResponse {
    private Long userId;
    private String username;
    private Integer totalXp;
    private Integer level;

    public AnonymousUserResponse(Long userId, String username, Integer totalXp, Integer level) {
        this.userId = userId;
        this.username = username;
        this.totalXp = totalXp;
        this.level = level;
    }

    public Long getUserId() { return userId; }
    public String getUsername() { return username; }
    public Integer getTotalXp() { return totalXp; }
    public Integer getLevel() { return level; }
}
