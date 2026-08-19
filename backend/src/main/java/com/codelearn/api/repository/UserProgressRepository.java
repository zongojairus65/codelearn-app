package com.codelearn.api.repository;

import com.codelearn.api.model.UserProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UserProgressRepository extends JpaRepository<UserProgress, Long> {
    List<UserProgress> findByUserId(Long userId);
    Optional<UserProgress> findByUserIdAndExerciseId(Long userId, Long exerciseId);
}
