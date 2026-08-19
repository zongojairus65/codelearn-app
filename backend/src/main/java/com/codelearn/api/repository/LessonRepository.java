package com.codelearn.api.repository;

import com.codelearn.api.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findByChapterIdOrderByOrderIndexAsc(Long chapterId);
}
