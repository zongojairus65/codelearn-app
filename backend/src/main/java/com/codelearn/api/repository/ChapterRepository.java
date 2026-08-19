package com.codelearn.api.repository;

import com.codelearn.api.model.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ChapterRepository extends JpaRepository<Chapter, Long> {
    List<Chapter> findAllByOrderByOrderIndexAsc();
}
