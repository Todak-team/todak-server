package com.todak.todak.domain.goal.controller;

import com.todak.todak.domain.goal.dto.GoalDto;
import com.todak.todak.domain.goal.service.GoalService;
import com.todak.todak.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/goals")
@RequiredArgsConstructor
@Tag(name = "Goal", description = "목표 API")
public class GoalController {

    private final GoalService goalService;

    @PostMapping
    @Operation(summary = "목표 등록")
    public ResponseEntity<ApiResponse<GoalDto.CreateResponse>> create(
            @AuthenticationPrincipal Long userId,
            @RequestBody GoalDto.CreateRequest request) {
        GoalDto.CreateResponse response = goalService.create(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }

    @GetMapping
    @Operation(summary = "목표 목록 조회")
    public ResponseEntity<ApiResponse<GoalDto.ListResponse>> getList(
            @AuthenticationPrincipal Long userId) {
        return ResponseEntity.ok(ApiResponse.success(goalService.getList(userId)));
    }

    @PatchMapping("/{goalId}/complete")
    @Operation(summary = "목표 완료 처리")
    public ResponseEntity<ApiResponse<GoalDto.CompleteResponse>> complete(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long goalId,
            @RequestBody GoalDto.CompleteRequest request) {
        return ResponseEntity.ok(ApiResponse.success(goalService.complete(userId, goalId, request)));
    }

    @DeleteMapping("/{goalId}")
    @Operation(summary = "목표 삭제")
    public ResponseEntity<ApiResponse<Map<String, String>>> delete(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long goalId) {
        goalService.delete(userId, goalId);
        return ResponseEntity.ok(ApiResponse.success(Map.of("message", "목표가 삭제되었습니다")));
    }
}