package com.codelearn.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_progress", uniqueConstraints =
    @UniqueConstraint(columnNames = {"user_id", "exercise_id"}))
public class UserProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id", nullable = false)
    private Exercise exercise;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProgressStatus status = ProgressStatus.LOCKED;

    private Integer score;
    private Integer xpEarned;
    private LocalDateTime completedAt;

    public enum ProgressStatus {
        LOCKED, UNLOCKED, COMPLETED
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Exercise getExercise() { return exercise; }
    public void setExercise(Exercise exercise) { this.exercise = exercise; }
    public ProgressStatus getStatus() { return status; }
    public void setStatus(ProgressStatus status) { this.status = status; }
    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }
    public Integer getXpEarned() { return xpEarned; }
    public void setXpEarned(Integer xpEarned) { this.xpEarned = xpEarned; }
    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
}
