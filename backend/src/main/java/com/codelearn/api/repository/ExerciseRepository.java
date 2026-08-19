package com.codelearn.api.repository;

import com.codelearn.api.model.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    List<Exercise> findByLessonIdOrderByOrderIndexAsc(Long lessonId);
}
