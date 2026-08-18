package com.codelearn.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "exercises")
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String instructions;

    @Column(columnDefinition = "TEXT")
    private String starterHtml;

    @Column(columnDefinition = "TEXT")
    private String starterCss;

    @Column(columnDefinition = "TEXT")
    private String starterJs;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String validationRules;

    @Column(nullable = false)
    private Integer orderIndex;

    @Column(nullable = false)
    private Integer xpReward = 15;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Lesson getLesson() { return lesson; }
    public void setLesson(Lesson lesson) { this.lesson = lesson; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getInstructions() { return instructions; }
    public void setInstructions(String instructions) { this.instructions = instructions; }
    public String getStarterHtml() { return starterHtml; }
    public void setStarterHtml(String starterHtml) { this.starterHtml = starterHtml; }
    public String getStarterCss() { return starterCss; }
    public void setStarterCss(String starterCss) { this.starterCss = starterCss; }
    public String getStarterJs() { return starterJs; }
    public void setStarterJs(String starterJs) { this.starterJs = starterJs; }
    public String getValidationRules() { return validationRules; }
    public void setValidationRules(String validationRules) { this.validationRules = validationRules; }
    public Integer getOrderIndex() { return orderIndex; }
    public void setOrderIndex(Integer orderIndex) { this.orderIndex = orderIndex; }
    public Integer getXpReward() { return xpReward; }
    public void setXpReward(Integer xpReward) { this.xpReward = xpReward; }
}
