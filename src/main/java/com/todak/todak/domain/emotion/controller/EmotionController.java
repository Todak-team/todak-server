package com.todak.todak.domain.emotion.controller;

import com.todak.todak.domain.emotion.dto.EmotionDto;
import com.todak.todak.domain.emotion.service.EmotionService;
import com.todak.todak.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/emotions")
@RequiredArgsConstructor
@Tag(name = "Emotion", description = "감정 체크인 API")
public class EmotionController {

    private final EmotionService emotionService;

    @PostMapping
    @Operation(summary = "오늘 감정 체크인 등록")
    public ResponseEntity<ApiResponse<EmotionDto.CheckInResponse>> checkIn(
            @AuthenticationPrincipal Long userId,
            @RequestBody EmotionDto.CheckInRequest request) {
        EmotionDto.CheckInResponse response = emotionService.checkIn(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }

    @PutMapping("/today")
    @Operation(summary = "오늘 감정 수정")
    public ResponseEntity<ApiResponse<EmotionDto.UpdateResponse>> updateToday(
            @AuthenticationPrincipal Long userId,
            @RequestBody EmotionDto.UpdateRequest request) {
        EmotionDto.UpdateResponse response = emotionService.updateToday(userId, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/today")
    @Operation(summary = "오늘 감정 조회")
    public ResponseEntity<ApiResponse<EmotionDto.TodayResponse>> getToday(
            @AuthenticationPrincipal Long userId) {
        Optional<EmotionDto.TodayResponse> response = emotionService.getToday(userId);
        if (response.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(ApiResponse.success(response.get()));
    }
}