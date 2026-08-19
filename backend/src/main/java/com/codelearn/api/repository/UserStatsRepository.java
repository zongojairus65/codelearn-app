package com.codelearn.api.repository;

import com.codelearn.api.model.UserStats;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserStatsRepository extends JpaRepository<UserStats, Long> {
}
