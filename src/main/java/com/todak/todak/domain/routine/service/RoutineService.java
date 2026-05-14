package com.todak.todak.domain.routine.service;

import com.todak.todak.domain.emotion.entity.DailyEmotion;
import com.todak.todak.domain.emotion.repository.DailyEmotionRepository;
import com.todak.todak.domain.routine.dto.RoutineDto;
import com.todak.todak.domain.routine.entity.Routine;
import com.todak.todak.domain.routine.entity.RoutineLog;
import com.todak.todak.domain.routine.repository.RoutineLogRepository;
import com.todak.todak.domain.routine.repository.RoutineRepository;
import com.todak.todak.domain.user.entity.User;
import com.todak.todak.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoutineService {

    private final RoutineRepository routineRepository;
    private final RoutineLogRepository routineLogRepository;
    private final DailyEmotionRepository dailyEmotionRepository;
    private final UserRepository userRepository;

    @Transactional
    public RoutineDto.CreateResponse create(Long userId, RoutineDto.CreateRequest request) {
        if (request.getPlanATitle() == null || request.getPlanATitle().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Plan A는 필수 입력값입니다");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다"));

        Routine routine = Routine.builder()
                .user(user)
                .planATitle(request.getPlanATitle())
                .planBTitle(request.getPlanBTitle())
                .build();

        Routine saved = routineRepository.save(routine);

        return RoutineDto.CreateResponse.builder()
                .routineId(saved.getRoutineId())
                .planATitle(saved.getPlanATitle())
                .planBTitle(saved.getPlanBTitle())
                .createdAt(saved.getCreatedAt())
                .build();
    }

    @Transactional(readOnly = true)
    public RoutineDto.ListResponse getList(Long userId) {
        Integer emotionScore = dailyEmotionRepository
                .findByUserUserIdAndCheckedDate(userId, LocalDate.now())
                .map(DailyEmotion::getEmotionScore)
                .orElse(null);

        List<Routine> routines = routineRepository.findByUserUserId(userId);

        List<RoutineDto.RoutineItem> items = routines.stream().map(routine -> {
            var logOpt = routineLogRepository.findByRoutineRoutineIdAndUserUserIdAndLogDate(
                    routine.getRoutineId(), userId, LocalDate.now());

            return RoutineDto.RoutineItem.builder()
                    .routineId(routine.getRoutineId())
                    .planATitle(routine.getPlanATitle())
                    .planBTitle(routine.getPlanBTitle())
                    .todayLog(RoutineDto.RoutineItem.TodayLog.builder()
                            .isCompleted(logOpt.map(RoutineLog::getIsCompleted).orElse(false))
                            .completedPlan(logOpt.map(RoutineLog::getCompletedPlan).orElse(null))
                            .build())
                    .build();
        }).collect(Collectors.toList());

        return RoutineDto.ListResponse.builder()
                .emotionScore(emotionScore)
                .routines(items)
                .build();
    }

    @Transactional
    public RoutineDto.CompleteResponse complete(Long userId, Long routineId, RoutineDto.CompleteRequest request) {
        Routine routine = routineRepository.findById(routineId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 루틴을 찾을 수 없습니다"));

        LocalDate logDate = request.getLogDate() != null ? request.getLogDate() : LocalDate.now();

        if (routineLogRepository.findByRoutineRoutineIdAndUserUserIdAndLogDate(routineId, userId, logDate).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "오늘 이미 완료 처리된 루틴입니다");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다"));

        RoutineLog log = RoutineLog.builder()
                .routine(routine)
                .user(user)
                .logDate(logDate)
                .isCompleted(true)
                .completedPlan(request.getCompletedPlan())
                .emotionScore(request.getEmotionScore())
                .build();

        RoutineLog saved = routineLogRepository.save(log);

        return RoutineDto.CompleteResponse.builder()
                .logId(saved.getLogId())
                .routineId(routineId)
                .isCompleted(true)
                .completedPlan(saved.getCompletedPlan())
                .logDate(saved.getLogDate())
                .build();
    }

    @Transactional
    public void delete(Long userId, Long routineId) {
        Routine routine = routineRepository.findById(routineId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 루틴을 찾을 수 없습니다"));
        routineRepository.delete(routine);
    }
}