package com.todak.todak.domain.routine.repository;

import com.todak.todak.domain.routine.entity.RoutineLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RoutineLogRepository extends JpaRepository<RoutineLog, Long> {
    Optional<RoutineLog> findByRoutineRoutineIdAndUserUserIdAndLogDate(
            Long routineId, Long userId, LocalDate logDate);

    List<RoutineLog> findByUserUserIdAndLogDateBetween(Long userId, LocalDate start, LocalDate end);

    List<RoutineLog> findByUserUserIdAndLogDate(Long userId, LocalDate logDate);

    void deleteAllByRoutineRoutineId(Long routineId);

    @Query("SELECT rl FROM RoutineLog rl JOIN FETCH rl.routine WHERE rl.user.userId = :userId AND rl.logDate BETWEEN :start AND :end")
    List<RoutineLog> findWithRoutineByUserIdAndDateBetween(@Param("userId") Long userId, @Param("start") LocalDate start, @Param("end") LocalDate end);
}