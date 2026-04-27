package com.todak.todak.domain.auth.service;

import com.todak.todak.domain.auth.client.KakaoAuthClient;
import com.todak.todak.domain.auth.dto.AuthDto;
import com.todak.todak.domain.auth.dto.KakaoTokenResponse;
import com.todak.todak.domain.auth.dto.KakaoUserInfoResponse;
import com.todak.todak.domain.user.entity.User;
import com.todak.todak.domain.user.repository.UserRepository;
import com.todak.todak.global.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final KakaoAuthClient kakaoAuthClient;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    /**
     * 카카오 소셜 로그인
     *
     * 처리 흐름:
     * 1. 인가 코드 → 카카오 액세스 토큰 교환
     * 2. 카카오 액세스 토큰 → 유저 정보 조회
     * 3. DB에서 kakao_id로 유저 검색
     *    - 신규: user 테이블 INSERT
     *    - 기존: 그대로 통과
     * 4. JWT Access Token 발급 후 리턴
     */
    @Transactional
    public AuthDto.KakaoLoginResponse kakaoLogin(String authorizationCode) {

        // 1. 인가 코드 → 카카오 액세스 토큰
        KakaoTokenResponse kakaoToken = kakaoAuthClient.getToken(authorizationCode);
        log.info("카카오 토큰 발급 완료");

        // 2. 카카오 액세스 토큰 → 유저 정보
        KakaoUserInfoResponse userInfo = kakaoAuthClient.getUserInfo(kakaoToken.getAccessToken());
        String kakaoId = userInfo.getKakaoId();
        String nickname = userInfo.getNickname();
        log.info("카카오 유저 정보 조회 완료: kakaoId={}, nickname={}", kakaoId, nickname);

        // 3. 신규/기존 유저 분기
        boolean isNewUser = !userRepository.existsByKakaoId(kakaoId);
        User user;

        if (isNewUser) {
            user = userRepository.save(
                    User.builder()
                            .kakaoId(kakaoId)
                            .nickname(nickname)
                            .build()
            );
            log.info("신규 유저 등록: userId={}", user.getUserId());
        } else {
            user = userRepository.findByKakaoId(kakaoId)
                    .orElseThrow(() -> new IllegalStateException("유저를 찾을 수 없습니다"));
            log.info("기존 유저 로그인: userId={}", user.getUserId());
        }

        // 4. JWT 발급
        String accessToken = jwtProvider.generateToken(user.getUserId());

        return AuthDto.KakaoLoginResponse.builder()
                .accessToken(accessToken)
                .nickname(user.getNickname())
                .isNewUser(isNewUser)
                .build();
    }
}