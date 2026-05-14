package com.todak.todak.domain.alert.controller;

import com.todak.todak.domain.alert.dto.AlertDto;
import com.todak.todak.domain.alert.service.AlertService;
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
@RequestMapping("/api/alerts")
@RequiredArgsConstructor
@Tag(name = "Alert", description = "알림 API")
public class AlertController {

    private final AlertService alertService;

    @GetMapping("/check")
    @Operation(summary = "3일 연속 방전 감지")
    public ResponseEntity<ApiResponse<AlertDto.CheckResponse>> check(
            @AuthenticationPrincipal Long userId) {
        return ResponseEntity.ok(ApiResponse.success(alertService.check(userId)));
    }
}