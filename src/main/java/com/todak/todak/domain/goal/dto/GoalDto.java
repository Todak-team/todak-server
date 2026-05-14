package com.todak.todak.domain.goal.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class GoalDto {

    @Getter
    public static class CreateRequest {
        @JsonProperty("plan_a_title")
        private String planATitle;

        @JsonProperty("plan_b_title")
        private String planBTitle;

        @JsonProperty("due_date")
        private LocalDate dueDate;
    }

    @Getter
    @Builder
    public static class CreateResponse {
        @JsonProperty("goal_id")
        private Long goalId;

        @JsonProperty("plan_a_title")
        private String planATitle;

        @JsonProperty("plan_b_title")
        private String planBTitle;

        @JsonProperty("due_date")
        private LocalDate dueDate;

        @JsonProperty("created_at")
        private LocalDateTime createdAt;
    }

    @Getter
    @Builder
    public static class GoalItem {
        @JsonProperty("goal_id")
        private Long goalId;

        @JsonProperty("plan_a_title")
        private String planATitle;

        @JsonProperty("plan_b_title")
        private String planBTitle;

        @JsonProperty("due_date")
        private LocalDate dueDate;

        @JsonProperty("is_completed")
        private Boolean isCompleted;

        @JsonProperty("completed_plan")
        private String completedPlan;
    }

    @Getter
    @Builder
    public static class ListResponse {
        @JsonProperty("emotion_score")
        private Integer emotionScore;

        @JsonProperty("goals")
        private List<GoalItem> goals;
    }

    @Getter
    public static class CompleteRequest {
        @JsonProperty("completed_plan")
        private String completedPlan;
    }

    @Getter
    @Builder
    public static class CompleteResponse {
        @JsonProperty("goal_id")
        private Long goalId;

        @JsonProperty("is_completed")
        private Boolean isCompleted;

        @JsonProperty("completed_plan")
        private String completedPlan;
    }
}