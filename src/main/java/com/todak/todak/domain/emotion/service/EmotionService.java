package com.todak.todak.domain.emotion.service;

import com.todak.todak.domain.emotion.dto.EmotionDto;
import com.todak.todak.domain.emotion.entity.DailyEmotion;
import com.todak.todak.domain.emotion.repository.DailyEmotionRepository;
import com.todak.todak.domain.user.entity.User;
import com.todak.todak.domain.user.repository.UserRepository;
import com.todak.todak.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmotionService {

    private final DailyEmotionRepository dailyEmotionRepository;
    private final UserRepository userRepository;

    @Transactional
    public EmotionDto.CheckinResponse checkin(Long userId, EmotionDto.CheckinRequest request) {
        dailyEmotionRepository.findByUserUserIdAndCheckedDate(userId, request.getCheckedDate())
                .ifPresent(e -> {
                    throw new BusinessException(HttpStatus.CONFLICT, "오늘은 이미 체크인했습니다. 수정 API를 사용해주세요");
                });

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(HttpStatus.UNAUTHORIZED, "유효하지 않은 사용자입니다"));

        DailyEmotion saved = dailyEmotionRepository.save(
                DailyEmotion.builder()
                        .user(user)
                        .emotionScore(request.getEmotionScore())
                        .checkedDate(request.getCheckedDate())
                        .build()
        );

        return EmotionDto.CheckinResponse.builder()
                .emotionId(saved.getEmotionId())
                .emotionScore(saved.getEmotionScore())
                .checkedDate(saved.getCheckedDate())
                .build();
    }

    @Transactional
    public EmotionDto.UpdateResponse updateToday(Long userId, EmotionDto.UpdateRequest request) {
        DailyEmotion emotion = dailyEmotionRepository
                .findByUserUserIdAndCheckedDate(userId, LocalDate.now())
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "오늘 감정 기록이 없습니다. 먼저 체크인해주세요"));

        emotion.update(request.getEmotionScore());

        return EmotionDto.UpdateResponse.builder()
                .emotionScore(emotion.getEmotionScore())
                .checkedDate(emotion.getCheckedDate())
                .updatedAt(emotion.getUpdatedAt())
                .build();
    }

    @Transactional(readOnly = true)
    public Optional<EmotionDto.TodayResponse> getToday(Long userId) {
        return dailyEmotionRepository
                .findByUserUserIdAndCheckedDate(userId, LocalDate.now())
                .map(e -> EmotionDto.TodayResponse.builder()
                        .emotionScore(e.getEmotionScore())
                        .checkedDate(e.getCheckedDate())
                        .build());
    }
}
