package com.todak.todak.domain.routine.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class RoutineDto {

    @Getter
    public static class CreateRequest {
        @JsonProperty("plan_a_title")
        private String planATitle;

        @JsonProperty("plan_b_title")
        private String planBTitle;
    }

    @Getter
    @Builder
    public static class CreateResponse {
        @JsonProperty("routine_id")
        private Long routineId;

        @JsonProperty("plan_a_title")
        private String planATitle;

        @JsonProperty("plan_b_title")
        private String planBTitle;

        @JsonProperty("created_at")
        private LocalDateTime createdAt;
    }

    @Getter
    @Builder
    public static class RoutineItem {
        @JsonProperty("routine_id")
        private Long routineId;

        @JsonProperty("plan_a_title")
        private String planATitle;

        @JsonProperty("plan_b_title")
        private String planBTitle;

        @JsonProperty("today_log")
        private TodayLog todayLog;

        @Getter
        @Builder
        public static class TodayLog {
            @JsonProperty("is_completed")
            private Boolean isCompleted;

            @JsonProperty("completed_plan")
            private String completedPlan;
        }
    }

    @Getter
    @Builder
    public static class ListResponse {
        @JsonProperty("emotion_score")
        private Integer emotionScore;

        @JsonProperty("routines")
        private List<RoutineItem> routines;
    }

    @Getter
    public static class CompleteRequest {
        @JsonProperty("completed_plan")
        private String completedPlan;

        @JsonProperty("log_date")
        private LocalDate logDate;

        @JsonProperty("emotion_score")
        private Integer emotionScore;
    }

    @Getter
    @Builder
    public static class CompleteResponse {
        @JsonProperty("log_id")
        private Long logId;

        @JsonProperty("routine_id")
        private Long routineId;

        @JsonProperty("is_completed")
        private Boolean isCompleted;

        @JsonProperty("completed_plan")
        private String completedPlan;

        @JsonProperty("log_date")
        private LocalDate logDate;
    }
}