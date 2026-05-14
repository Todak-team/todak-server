package com.todak.todak.domain.goal.repository;

import com.todak.todak.domain.goal.entity.Goal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GoalRepository extends JpaRepository<Goal, Long> {
    List<Goal> findByUserUserIdAndIsCompletedFalseOrderByDueDateAsc(Long userId);
}