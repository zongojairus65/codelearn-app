package com.codelearn.api.dto;

import com.codelearn.api.model.Exercise;

public class ExerciseDTO {
    private Long id;
    private String title;
    private String instructions;
    private String starterHtml;
    private String starterCss;
    private String starterJs;
    private Integer xpReward;

    public static ExerciseDTO fromEntity(Exercise exercise) {
        ExerciseDTO dto = new ExerciseDTO();
        dto.id = exercise.getId();
        dto.title = exercise.getTitle();
        dto.instructions = exercise.getInstructions();
        dto.starterHtml = exercise.getStarterHtml();
        dto.starterCss = exercise.getStarterCss();
        dto.starterJs = exercise.getStarterJs();
        dto.xpReward = exercise.getXpReward();
        return dto;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getInstructions() { return instructions; }
    public String getStarterHtml() { return starterHtml; }
    public String getStarterCss() { return starterCss; }
    public String getStarterJs() { return starterJs; }
    public Integer getXpReward() { return xpReward; }
}
