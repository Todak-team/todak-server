package com.todak.todak.domain.emotion.repository;

import com.todak.todak.domain.emotion.entity.DailyEmotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DailyEmotionRepository extends JpaRepository<DailyEmotion, Long> {

    Optional<DailyEmotion> findByUserUserIdAndCheckedDate(Long userId, LocalDate checkedDate);

    @Query("SELECT e FROM DailyEmotion e WHERE e.user.userId = :userId ORDER BY e.checkedDate DESC LIMIT 3")
    List<DailyEmotion> findTop3ByUserIdOrderByCheckedDateDesc(@Param("userId") Long userId);

    List<DailyEmotion> findByUserUserIdAndCheckedDateBetweenOrderByCheckedDateAsc(
            Long userId, LocalDate start, LocalDate end);
}