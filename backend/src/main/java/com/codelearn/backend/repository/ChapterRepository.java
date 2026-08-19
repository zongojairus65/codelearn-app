package com.codelearn.backend.repository;

import com.codelearn.backend.model.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ChapterRepository extends JpaRepository<Chapter, Long> {
    List<Chapter> findAllByOrderByOrderIndexAsc();
}
