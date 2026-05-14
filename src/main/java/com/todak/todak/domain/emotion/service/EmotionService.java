package com.todak.todak.domain.emotion.service;

import com.todak.todak.domain.emotion.dto.EmotionDto;
import com.todak.todak.domain.emotion.entity.DailyEmotion;
import com.todak.todak.domain.emotion.repository.DailyEmotionRepository;
import com.todak.todak.domain.user.entity.User;
import com.todak.todak.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmotionService {

    private final DailyEmotionRepository dailyEmotionRepository;
    private final UserRepository userRepository;

    @Transactional
    public EmotionDto.CheckInResponse checkIn(Long userId, EmotionDto.CheckInRequest request) {
        if (request.getEmotionScore() < 1 || request.getEmotionScore() > 5) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "감정 레벨은 1~5 사이여야 합니다");
        }

        LocalDate date = request.getCheckedDate() != null ? request.getCheckedDate() : LocalDate.now();

        if (dailyEmotionRepository.findByUserUserIdAndCheckedDate(userId, date).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "오늘은 이미 체크인했습니다. 수정 API를 사용해주세요");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다"));

        DailyEmotion emotion = DailyEmotion.builder()
                .user(user)
                .emotionScore(request.getEmotionScore())
                .checkedDate(date)
                .build();

        DailyEmotion saved = dailyEmotionRepository.save(emotion);

        return EmotionDto.CheckInResponse.builder()
                .emotionId(saved.getEmotionId())
                .emotionScore(saved.getEmotionScore())
                .checkedDate(saved.getCheckedDate())
                .build();
    }

    @Transactional
    public EmotionDto.UpdateResponse updateToday(Long userId, EmotionDto.UpdateRequest request) {
        if (request.getEmotionScore() < 1 || request.getEmotionScore() > 5) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "감정 레벨은 1~5 사이여야 합니다");
        }

        DailyEmotion emotion = dailyEmotionRepository.findByUserUserIdAndCheckedDate(userId, LocalDate.now())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "오늘 감정 기록이 없습니다. 먼저 체크인해주세요"));

        emotion.updateScore(request.getEmotionScore());

        return EmotionDto.UpdateResponse.builder()
                .emotionScore(emotion.getEmotionScore())
                .checkedDate(emotion.getCheckedDate())
                .updatedAt(emotion.getUpdatedAt())
                .build();
    }

    @Transactional(readOnly = true)
    public Optional<EmotionDto.TodayResponse> getToday(Long userId) {
        return dailyEmotionRepository.findByUserUserIdAndCheckedDate(userId, LocalDate.now())
                .map(e -> EmotionDto.TodayResponse.builder()
                        .emotionScore(e.getEmotionScore())
                        .checkedDate(e.getCheckedDate())
                        .build());
    }
}