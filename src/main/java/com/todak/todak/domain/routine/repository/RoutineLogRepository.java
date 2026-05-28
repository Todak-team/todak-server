package com.todak.todak.domain.routine.repository;

import com.todak.todak.domain.routine.entity.RoutineLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RoutineLogRepository extends JpaRepository<RoutineLog, Long> {
    Optional<RoutineLog> findByRoutineRoutineIdAndUserUserIdAndLogDate(
            Long routineId, Long userId, LocalDate logDate);

    List<RoutineLog> findByUserUserIdAndLogDateBetween(Long userId, LocalDate start, LocalDate end);

    List<RoutineLog> findByUserUserIdAndLogDate(Long userId, LocalDate logDate);
}