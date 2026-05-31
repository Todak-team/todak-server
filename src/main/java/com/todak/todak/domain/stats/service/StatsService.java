package com.todak.todak.domain.stats.service;

import com.todak.todak.domain.emotion.entity.DailyEmotion;
import com.todak.todak.domain.emotion.repository.DailyEmotionRepository;
import com.todak.todak.domain.routine.entity.RoutineLog;
import com.todak.todak.domain.routine.repository.RoutineLogRepository;
import com.todak.todak.domain.stats.dto.StatsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatsService {

    private final RoutineLogRepository routineLogRepository;
    private final DailyEmotionRepository dailyEmotionRepository;

    private static final String[] DAY_LABELS = {"월", "화", "수", "목", "금", "토", "일"};

    public StatsDto.WeeklyResponse getWeekly(Long userId) {
        LocalDate today = LocalDate.now();
        LocalDate thisMonday = today.with(DayOfWeek.MONDAY);
        LocalDate lastMonday = thisMonday.minusWeeks(1);
        LocalDate lastSunday = thisMonday.minusDays(1);

        List<RoutineLog> thisWeekLogs = routineLogRepository.findByUserUserIdAndLogDateBetween(userId, thisMonday, today);
        int weeklyAchievementPct = calcPct(thisWeekLogs);

        List<RoutineLog> lastWeekLogs = routineLogRepository.findByUserUserIdAndLogDateBetween(userId, lastMonday, lastSunday);
        int weeklyChange = lastWeekLogs.isEmpty() ? 0 : weeklyAchievementPct - calcPct(lastWeekLogs);

        LocalDate sevenDaysAgo = today.minusDays(6);
        List<RoutineLog> recentLogs = routineLogRepository.findByUserUserIdAndLogDateBetween(userId, sevenDaysAgo, today);
        List<DailyEmotion> recentEmotions = dailyEmotionRepository
                .findByUserUserIdAndCheckedDateBetweenOrderByCheckedDateAsc(userId, sevenDaysAgo, today);

        Map<LocalDate, List<RoutineLog>> logsByDate = recentLogs.stream()
                .collect(Collectors.groupingBy(RoutineLog::getLogDate));
        Map<LocalDate, Integer> emotionByDate = recentEmotions.stream()
                .collect(Collectors.toMap(DailyEmotion::getCheckedDate, DailyEmotion::getEmotionScore));

        List<StatsDto.DailyData> dailyData = new ArrayList<>();
        for (int i = 6; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            List<RoutineLog> dayLogs = logsByDate.getOrDefault(date, Collections.emptyList());
            dailyData.add(StatsDto.DailyData.builder()
                    .date(date.toString())
                    .dayLabel(DAY_LABELS[date.getDayOfWeek().getValue() - 1])
                    .achievementPct(calcPct(dayLogs))
                    .emotionLevel(emotionByDate.getOrDefault(date, 0))
                    .isToday(date.equals(today))
                    .build());
        }

        return StatsDto.WeeklyResponse.builder()
                .weeklyAchievementPct(weeklyAchievementPct)
                .weeklyChange(weeklyChange)
                .dailyData(dailyData)
                .build();
    }

    public StatsDto.SummaryResponse getSummary(Long userId) {
        LocalDate today = LocalDate.now();

        // consecutive_days
        List<RoutineLog> todayLogs = routineLogRepository.findByUserUserIdAndLogDate(userId, today);
        boolean todayCompleted = todayLogs.stream().anyMatch(l -> Boolean.TRUE.equals(l.getIsCompleted()));
        LocalDate checkDate = todayCompleted ? today : today.minusDays(1);
        int consecutiveDays = 0;
        for (int i = 0; i < 365; i++) {
            List<RoutineLog> logs = routineLogRepository.findByUserUserIdAndLogDate(userId, checkDate);
            if (logs.stream().noneMatch(l -> Boolean.TRUE.equals(l.getIsCompleted()))) break;
            consecutiveDays++;
            checkDate = checkDate.minusDays(1);
        }

        // avg_emotion_score (최근 30일)
        LocalDate thirtyDaysAgo = today.minusDays(29);
        List<DailyEmotion> emotions = dailyEmotionRepository
                .findByUserUserIdAndCheckedDateBetweenOrderByCheckedDateAsc(userId, thirtyDaysAgo, today);
        double avgEmotionScore = emotions.isEmpty() ? 0.0
                : Math.round(emotions.stream().mapToInt(DailyEmotion::getEmotionScore).average().orElse(0.0) * 10.0) / 10.0;

        // best_routine (최근 30일)
        List<RoutineLog> recentLogs = routineLogRepository.findWithRoutineByUserIdAndDateBetween(userId, thirtyDaysAgo, today);
        String bestRoutineName = "";
        int bestRoutineAchievementPct = 0;

        if (!recentLogs.isEmpty()) {
            Map<Long, List<RoutineLog>> byRoutine = recentLogs.stream()
                    .collect(Collectors.groupingBy(log -> log.getRoutine().getRoutineId()));

            int bestPct = -1;
            LocalDateTime bestCreatedAt = LocalDateTime.MIN;

            for (List<RoutineLog> routineLogs : byRoutine.values()) {
                int pct = calcPct(routineLogs);
                LocalDateTime createdAt = routineLogs.get(0).getRoutine().getCreatedAt();
                if (pct > bestPct || (pct == bestPct && createdAt.isAfter(bestCreatedAt))) {
                    bestPct = pct;
                    bestCreatedAt = createdAt;
                    bestRoutineName = routineLogs.get(0).getRoutine().getPlanATitle();
                    bestRoutineAchievementPct = pct;
                }
            }
        }

        return StatsDto.SummaryResponse.builder()
                .consecutiveDays(consecutiveDays)
                .avgEmotionScore(avgEmotionScore)
                .bestRoutineName(bestRoutineName)
                .bestRoutineAchievementPct(bestRoutineAchievementPct)
                .build();
    }

    private int calcPct(List<RoutineLog> logs) {
        if (logs.isEmpty()) return 0;
        long completed = logs.stream().filter(l -> Boolean.TRUE.equals(l.getIsCompleted())).count();
        return (int) Math.round(completed * 100.0 / logs.size());
    }
}
