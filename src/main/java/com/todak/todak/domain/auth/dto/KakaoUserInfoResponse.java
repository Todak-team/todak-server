package com.todak.todak.domain.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * 카카오 유저 정보 API 응답
 * GET https://kapi.kakao.com/v2/user/me
 */
@Getter
public class KakaoUserInfoResponse {

    private Long id;  // kakao_id

    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    @Getter
    public static class KakaoAccount {
        private KakaoProfile profile;
    }

    @Getter
    public static class KakaoProfile {
        private String nickname;
    }

    public String getKakaoId() {
        return String.valueOf(id);
    }

    public String getNickname() {
        if (kakaoAccount != null && kakaoAccount.getProfile() != null) {
            return kakaoAccount.getProfile().getNickname();
        }
        return "토닥유저";
    }
}