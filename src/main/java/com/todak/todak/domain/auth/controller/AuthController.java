package com.todak.todak.domain.auth.controller;

import com.todak.todak.domain.auth.dto.AuthDto;
import com.todak.todak.domain.auth.service.AuthService;
import com.todak.todak.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth", description = "인증 API")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "카카오 소셜 로그인", description = "카카오 인가 코드를 전달받아 JWT를 발급합니다")
    @PostMapping("/kakao")
    public ResponseEntity<ApiResponse<AuthDto.KakaoLoginResponse>> kakaoLogin(
            @Valid @RequestBody AuthDto.KakaoLoginRequest request
    ) {
        AuthDto.KakaoLoginResponse response = authService.kakaoLogin(request.getKakaoCode());
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}