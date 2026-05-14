package com.todak.todak.domain.alert.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

public class AlertDto {

    @Getter
    @Builder
    public static class CheckResponse {
        @JsonProperty("show_alert")
        private Boolean showAlert;

        @JsonProperty("message")
        private String message;
    }
}