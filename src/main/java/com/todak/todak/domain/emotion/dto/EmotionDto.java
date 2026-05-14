package com.todak.todak.domain.emotion.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class EmotionDto {

    @Getter
    public static class CheckinRequest {

        @NotNull(message = "감정 레벨은 1~5 사이여야 합니다")
        @Min(value = 1, message = "감정 레벨은 1~5 사이여야 합니다")
        @Max(value = 5, message = "감정 레벨은 1~5 사이여야 합니다")
        @JsonProperty("emotion_score")
        private Integer emotionScore;

        @NotNull(message = "체크인 날짜는 필수입니다")
        @JsonProperty("checked_date")
        private LocalDate checkedDate;
    }

    @Getter
    @Builder
    public static class CheckinResponse {

        @JsonProperty("emotion_id")
        private Long emotionId;

        @JsonProperty("emotion_score")
        private int emotionScore;

        @JsonProperty("checked_date")
        private LocalDate checkedDate;
    }

    @Getter
    public static class UpdateRequest {

        @NotNull(message = "감정 레벨은 1~5 사이여야 합니다")
        @Min(value = 1, message = "감정 레벨은 1~5 사이여야 합니다")
        @Max(value = 5, message = "감정 레벨은 1~5 사이여야 합니다")
        @JsonProperty("emotion_score")
        private Integer emotionScore;
    }

    @Getter
    @Builder
    public static class UpdateResponse {

        @JsonProperty("emotion_score")
        private int emotionScore;

        @JsonProperty("checked_date")
        private LocalDate checkedDate;

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        @JsonProperty("updated_at")
        private LocalDateTime updatedAt;
    }

    @Getter
    @Builder
    public static class TodayResponse {

        @JsonProperty("emotion_score")
        private int emotionScore;

        @JsonProperty("checked_date")
        private LocalDate checkedDate;
    }
}
