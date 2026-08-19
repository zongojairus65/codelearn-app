package com.codelearn.api.model;

import jakarta.persistence.*;

@Entity
@Table(name = "lessons")
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chapter_id", nullable = false)
    private Chapter chapter;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String theoryContent;

    @Column(nullable = false)
    private Integer orderIndex;

    @Column(nullable = false)
    private Integer xpReward = 20;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Chapter getChapter() { return chapter; }
    public void setChapter(Chapter chapter) { this.chapter = chapter; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getTheoryContent() { return theoryContent; }
    public void setTheoryContent(String theoryContent) { this.theoryContent = theoryContent; }
    public Integer getOrderIndex() { return orderIndex; }
    public void setOrderIndex(Integer orderIndex) { this.orderIndex = orderIndex; }
    public Integer getXpReward() { return xpReward; }
    public void setXpReward(Integer xpReward) { this.xpReward = xpReward; }
}
