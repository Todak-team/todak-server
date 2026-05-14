package com.todak.todak.domain.emotion.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class EmotionDto {

    @Getter
    public static class CheckInRequest {
        @JsonProperty("emotion_score")
        private Integer emotionScore;

        @JsonProperty("checked_date")
        private LocalDate checkedDate;
    }

    @Getter
    public static class UpdateRequest {
        @JsonProperty("emotion_score")
        private Integer emotionScore;
    }

    @Getter
    @Builder
    public static class CheckInResponse {
        @JsonProperty("emotion_id")
        private Long emotionId;

        @JsonProperty("emotion_score")
        private Integer emotionScore;

        @JsonProperty("checked_date")
        private LocalDate checkedDate;
    }

    @Getter
    @Builder
    public static class UpdateResponse {
        @JsonProperty("emotion_score")
        private Integer emotionScore;

        @JsonProperty("checked_date")
        private LocalDate checkedDate;

        @JsonProperty("updated_at")
        private LocalDateTime updatedAt;
    }

    @Getter
    @Builder
    public static class TodayResponse {
        @JsonProperty("emotion_score")
        private Integer emotionScore;

        @JsonProperty("checked_date")
        private LocalDate checkedDate;
    }
}