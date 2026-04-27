package com.todak.todak.domain.auth.client;

import com.todak.todak.domain.auth.dto.KakaoTokenResponse;
import com.todak.todak.domain.auth.dto.KakaoUserInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class KakaoAuthClient {

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    /**
     * 1단계: 인가 코드 → 카카오 액세스 토큰 교환
     */
    public KakaoTokenResponse getToken(String authorizationCode) {
        return WebClient.create("https://kauth.kakao.com")
                .post()
                .uri("/oauth/token")
                .body(BodyInserters.fromFormData("grant_type", "authorization_code")
                        .with("client_id", clientId)
                        .with("redirect_uri", redirectUri)
                        .with("code", authorizationCode))
                .retrieve()
                .bodyToMono(KakaoTokenResponse.class)
                .block();
    }

    /**
     * 2단계: 카카오 액세스 토큰 → 유저 정보 조회
     */
    public KakaoUserInfoResponse getUserInfo(String kakaoAccessToken) {
        return WebClient.create("https://kapi.kakao.com")
                .get()
                .uri("/v2/user/me")
                .header("Authorization", "Bearer " + kakaoAccessToken)
                .retrieve()
                .bodyToMono(KakaoUserInfoResponse.class)
                .block();
    }
}