package com.todak.todak.domain.stats.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class StatsDto {

    @Getter
    @Builder
    public static class WeeklyResponse {
        @JsonProperty("weekly_achievement_pct")
        private int weeklyAchievementPct;

        @JsonProperty("weekly_change")
        private int weeklyChange;

        @JsonProperty("daily_data")
        private List<DailyData> dailyData;
    }

    @Getter
    @Builder
    public static class DailyData {
        @JsonProperty("date")
        private String date;

        @JsonProperty("day_label")
        private String dayLabel;

        @JsonProperty("achievement_pct")
        private int achievementPct;

        @JsonProperty("emotion_level")
        private int emotionLevel;

        @JsonProperty("is_today")
        private Boolean isToday;
    }

    @Getter
    @Builder
    public static class SummaryResponse {
        @JsonProperty("consecutive_days")
        private int consecutiveDays;

        @JsonProperty("avg_emotion_score")
        private double avgEmotionScore;

        @JsonProperty("best_routine_name")
        private String bestRoutineName;

        @JsonProperty("best_routine_achievement_pct")
        private int bestRoutineAchievementPct;
    }
}
