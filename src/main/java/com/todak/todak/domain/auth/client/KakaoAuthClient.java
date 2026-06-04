package com.todak.todak.domain.auth.client;

import com.todak.todak.domain.auth.dto.KakaoUserInfoResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class KakaoAuthClient {

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
