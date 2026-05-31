package com.todak.todak.domain.stats.controller;

import com.todak.todak.domain.stats.dto.StatsDto;
import com.todak.todak.domain.stats.service.StatsService;
import com.todak.todak.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
@Tag(name = "Stats", description = "대시보드 통계 API")
public class StatsController {

    private final StatsService statsService;

    @GetMapping("/weekly")
    @Operation(summary = "이번 주 성적표 및 최근 7일 달성도")
    public ResponseEntity<ApiResponse<StatsDto.WeeklyResponse>> getWeekly(
            @AuthenticationPrincipal Long userId) {
        return ResponseEntity.ok(ApiResponse.success(statsService.getWeekly(userId)));
    }

    @GetMapping("/summary")
    @Operation(summary = "내 기록 한눈에 (연속일, 평균 감정, 베스트 루틴)")
    public ResponseEntity<ApiResponse<StatsDto.SummaryResponse>> getSummary(
            @AuthenticationPrincipal Long userId) {
        return ResponseEntity.ok(ApiResponse.success(statsService.getSummary(userId)));
    }
}
