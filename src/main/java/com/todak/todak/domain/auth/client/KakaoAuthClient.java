package com.todak.todak.domain.auth.client;

import com.todak.todak.domain.auth.dto.KakaoTokenResponse;
import com.todak.todak.domain.auth.dto.KakaoUserInfoResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class KakaoAuthClient {

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    public KakaoTokenResponse getToken(String code) {
        return WebClient.create("https://kauth.kakao.com")
                .post()
                .uri("/oauth/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("grant_type", "authorization_code")
                        .with("client_id", clientId)
                        .with("redirect_uri", redirectUri)
                        .with("code", code))
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        response -> response.bodyToMono(String.class)
                                .flatMap(body -> {
                                    log.error("카카오 토큰 발급 실패: status={}, body={}", response.statusCode(), body);
                                    return Mono.error(new RuntimeException("카카오 토큰 발급 실패: " + body));
                                }))
                .bodyToMono(KakaoTokenResponse.class)
                .block();
    }

    public KakaoUserInfoResponse getUserInfo(String kakaoAccessToken) {
        return WebClient.create("https://kapi.kakao.com")
                .get()
                .uri("/v2/user/me")
                .header("Authorization", "Bearer " + kakaoAccessToken)
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        response -> response.bodyToMono(String.class)
                                .flatMap(body -> {
                                    log.error("카카오 유저 정보 조회 실패: status={}, body={}", response.statusCode(), body);
                                    return Mono.error(new RuntimeException("카카오 유저 정보 조회 실패: " + body));
                                }))
                .bodyToMono(KakaoUserInfoResponse.class)
                .block();
    }
}
