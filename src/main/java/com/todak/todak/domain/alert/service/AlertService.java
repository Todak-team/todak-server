package com.todak.todak.domain.alert.service;

import com.todak.todak.domain.alert.dto.AlertDto;
import com.todak.todak.domain.emotion.entity.DailyEmotion;
import com.todak.todak.domain.emotion.repository.DailyEmotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlertService {

    private final DailyEmotionRepository dailyEmotionRepository;

    @Transactional(readOnly = true)
    public AlertDto.CheckResponse check(Long userId) {
        List<DailyEmotion> recent = dailyEmotionRepository.findTop3ByUserIdOrderByCheckedDateDesc(userId);

        boolean showAlert = recent.size() == 3 &&
                recent.stream().allMatch(e -> e.getEmotionScore() <= 2);

        return AlertDto.CheckResponse.builder()
                .showAlert(showAlert)
                .message(showAlert
                        ? "3일 연속 많이 지치셨군요. 오늘은 아주 작은 것부터 시작해봐요 🌱"
                        : null)
                .build();
    }
}