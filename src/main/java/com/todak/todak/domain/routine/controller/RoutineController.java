package com.todak.todak.domain.routine.controller;

import com.todak.todak.domain.routine.dto.RoutineDto;
import com.todak.todak.domain.routine.service.RoutineService;
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
@RequestMapping("/api/routines")
@RequiredArgsConstructor
@Tag(name = "Routine", description = "루틴 API")
public class RoutineController {

    private final RoutineService routineService;

    @PostMapping
    @Operation(summary = "루틴 등록")
    public ResponseEntity<ApiResponse<RoutineDto.CreateResponse>> create(
            @AuthenticationPrincipal Long userId,
            @RequestBody RoutineDto.CreateRequest request) {
        RoutineDto.CreateResponse response = routineService.create(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }

    @GetMapping
    @Operation(summary = "루틴 목록 조회")
    public ResponseEntity<ApiResponse<RoutineDto.ListResponse>> getList(
            @AuthenticationPrincipal Long userId) {
        return ResponseEntity.ok(ApiResponse.success(routineService.getList(userId)));
    }

    @PatchMapping("/{routineId}/complete")
    @Operation(summary = "루틴 완료 처리")
    public ResponseEntity<ApiResponse<RoutineDto.CompleteResponse>> complete(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long routineId,
            @RequestBody RoutineDto.CompleteRequest request) {
        return ResponseEntity.ok(ApiResponse.success(routineService.complete(userId, routineId, request)));
    }

    @DeleteMapping("/{routineId}")
    @Operation(summary = "루틴 삭제")
    public ResponseEntity<ApiResponse<Map<String, String>>> delete(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long routineId) {
        routineService.delete(userId, routineId);
        return ResponseEntity.ok(ApiResponse.success(Map.of("message", "루틴이 삭제되었습니다")));
    }
}