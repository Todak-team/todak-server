package com.todak.todak.domain.auth.service;

import com.todak.todak.domain.auth.client.KakaoAuthClient;
import com.todak.todak.domain.auth.dto.AuthDto;
import com.todak.todak.domain.auth.dto.KakaoUserInfoResponse;
import com.todak.todak.domain.user.entity.User;
import com.todak.todak.domain.user.repository.UserRepository;
import com.todak.todak.global.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final KakaoAuthClient kakaoAuthClient;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    @Transactional
    public AuthDto.KakaoLoginResponse kakaoLogin(String accessToken) {
        KakaoUserInfoResponse userInfo = kakaoAuthClient.getUserInfo(accessToken);

        boolean isNewUser = !userRepository.existsByKakaoId(userInfo.getKakaoId());

        User user = userRepository.findByKakaoId(userInfo.getKakaoId())
                .orElseGet(() -> userRepository.save(User.builder()
                        .kakaoId(userInfo.getKakaoId())
                        .nickname(userInfo.getNickname())
                        .email(userInfo.getEmail())
                        .profileImageUrl(userInfo.getProfileImageUrl())
                        .build()));

        String jwtToken = jwtProvider.generateToken(user.getUserId());

        return AuthDto.KakaoLoginResponse.builder()
                .accessToken(jwtToken)
                .nickname(user.getNickname())
                .isNewUser(isNewUser)
                .build();
    }
}
