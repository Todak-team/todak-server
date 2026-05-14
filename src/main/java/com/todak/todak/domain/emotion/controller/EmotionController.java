package com.todak.todak.domain.emotion.controller;

import com.todak.todak.domain.emotion.dto.EmotionDto;
import com.todak.todak.domain.emotion.service.EmotionService;
import com.todak.todak.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Emotion", description = "감정 체크인 API")
@RestController
@RequestMapping("/api/emotions")
@RequiredArgsConstructor
public class EmotionController {

    private final EmotionService emotionService;

    @Operation(summary = "오늘 감정 체크인 등록", description = "하루 1회 감정 점수를 등록합니다")
    @PostMapping
    public ResponseEntity<ApiResponse<EmotionDto.CheckinResponse>> checkin(
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody EmotionDto.CheckinRequest request
    ) {
        EmotionDto.CheckinResponse response = emotionService.checkin(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }

    @Operation(summary = "오늘 감정 수정", description = "오늘 등록한 감정 점수를 수정합니다")
    @PutMapping("/today")
    public ResponseEntity<ApiResponse<EmotionDto.UpdateResponse>> updateToday(
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody EmotionDto.UpdateRequest request
    ) {
        EmotionDto.UpdateResponse response = emotionService.updateToday(userId, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "오늘 감정 조회", description = "오늘 체크인한 감정 점수를 조회합니다. 미체크인 시 204 반환")
    @GetMapping("/today")
    public ResponseEntity<ApiResponse<EmotionDto.TodayResponse>> getToday(
            @AuthenticationPrincipal Long userId
    ) {
        return emotionService.getToday(userId)
                .map(data -> ResponseEntity.ok(ApiResponse.success(data)))
                .orElse(ResponseEntity.noContent().build());
    }
}
