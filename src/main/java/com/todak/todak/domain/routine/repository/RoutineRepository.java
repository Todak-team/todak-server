package com.todak.todak.domain.routine.repository;

import com.todak.todak.domain.routine.entity.Routine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoutineRepository extends JpaRepository<Routine, Long> {

    List<Routine> findByUserUserId(Long userId);
}
