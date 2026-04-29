package com.todak.todak.domain.emotion.repository;

import com.todak.todak.domain.emotion.entity.DailyEmotion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DailyEmotionRepository extends JpaRepository<DailyEmotion, Long> {

    Optional<DailyEmotion> findByUserUserIdAndCheckedDate(Long userId, LocalDate checkedDate);

    List<DailyEmotion> findByUserUserIdOrderByCheckedDateDesc(Long userId);
}
