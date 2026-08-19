package com.codelearn.api.service;

import com.codelearn.api.dto.CompleteExerciseResponse;
import com.codelearn.api.dto.LessonProgressDTO;
import com.codelearn.api.model.*;
import com.codelearn.api.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ProgressService {

    private final ExerciseRepository exerciseRepository;
    private final LessonRepository lessonRepository;
    private final UserProgressRepository userProgressRepository;
    private final UserStatsRepository userStatsRepository;

    public ProgressService(ExerciseRepository exerciseRepository,
                            LessonRepository lessonRepository,
                            UserProgressRepository userProgressRepository,
                            UserStatsRepository userStatsRepository) {
        this.exerciseRepository = exerciseRepository;
        this.lessonRepository = lessonRepository;
        this.userProgressRepository = userProgressRepository;
        this.userStatsRepository = userStatsRepository;
    }

    public CompleteExerciseResponse completeExercise(Long userId, Long exerciseId, Integer score) {
        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new IllegalArgumentException("Exercice introuvable"));
        Lesson lesson = exercise.getLesson();
        Chapter chapter = lesson.getChapter();

        UserStats stats = userStatsRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable"));

        Optional<UserProgress> existing = userProgressRepository.findByUserIdAndExerciseId(userId, exerciseId);
        boolean alreadyCompleted = existing.isPresent()
                && existing.get().getStatus() == UserProgress.ProgressStatus.COMPLETED;

        int xpEarned = 0;
        if (!alreadyCompleted) {
            xpEarned = exercise.getXpReward() + lesson.getXpReward();

            UserProgress progress = existing.orElseGet(UserProgress::new);
            if (progress.getId() == null) {
                progress.setUser(stats.getUser());
                progress.setExercise(exercise);
            }
            progress.setStatus(UserProgress.ProgressStatus.COMPLETED);
            progress.setScore(score);
            progress.setXpEarned(xpEarned);
            progress.setCompletedAt(java.time.LocalDateTime.now());
            userProgressRepository.save(progress);

            stats.setTotalXp(stats.getTotalXp() + xpEarned);
            stats.setLevel(1 + (int) Math.sqrt(stats.getTotalXp() / 50.0));
            updateStreak(stats);
            userStatsRepository.save(stats);
        }

        List<Lesson> chapterLessons = lessonRepository.findByChapterIdOrderByOrderIndexAsc(chapter.getId());
        Long nextLessonId = null;
        boolean chapterCompleted = true;

        for (int i = 0; i < chapterLessons.size(); i++) {
            Lesson l = chapterLessons.get(i);
            boolean isCompleted = isLessonCompleted(userId, l);
            if (!isCompleted) chapterCompleted = false;
            if (l.getId().equals(lesson.getId()) && i + 1 < chapterLessons.size()) {
                nextLessonId = chapterLessons.get(i + 1).getId();
            }
        }

        return new CompleteExerciseResponse(
                xpEarned, stats.getTotalXp(), stats.getLevel(),
                stats.getCurrentStreak(), nextLessonId, chapterCompleted
        );
    }

    public List<LessonProgressDTO> getChapterProgress(Long userId, Long chapterId) {
        List<Lesson> lessons = lessonRepository.findByChapterIdOrderByOrderIndexAsc(chapterId);
        return lessons.stream().map(lesson -> {
            boolean completed = isLessonCompleted(userId, lesson);
            String status;
            if (completed) {
                status = "COMPLETED";
            } else if (lesson.getOrderIndex() == 1) {
                status = "UNLOCKED";
            } else {
                Lesson previous = lessons.get(lesson.getOrderIndex() - 2);
                status = isLessonCompleted(userId, previous) ? "UNLOCKED" : "LOCKED";
            }
            return new LessonProgressDTO(lesson.getId(), lesson.getTitle(), lesson.getOrderIndex(), lesson.getXpReward(), status);
        }).toList();
    }

    private boolean isLessonCompleted(Long userId, Lesson lesson) {
        List<Exercise> exercises = exerciseRepository.findByLessonIdOrderByOrderIndexAsc(lesson.getId());
        if (exercises.isEmpty()) return false;
        return exercises.stream().allMatch(ex ->
                userProgressRepository.findByUserIdAndExerciseId(userId, ex.getId())
                        .map(p -> p.getStatus() == UserProgress.ProgressStatus.COMPLETED)
                        .orElse(false)
        );
    }

    private void updateStreak(UserStats stats) {
        LocalDate today = LocalDate.now();
        LocalDate last = stats.getLastActivityDate();
        if (last == null || !last.equals(today)) {
            if (last != null && last.equals(today.minusDays(1))) {
                stats.setCurrentStreak(stats.getCurrentStreak() + 1);
            } else if (last == null || !last.equals(today)) {
                stats.setCurrentStreak(last != null ? 1 : 1);
            }
            stats.setLastActivityDate(today);
            if (stats.getCurrentStreak() > stats.getLongestStreak()) {
                stats.setLongestStreak(stats.getCurrentStreak());
            }
        }
    }
}
