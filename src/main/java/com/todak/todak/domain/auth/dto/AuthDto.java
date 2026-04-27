package com.todak.todak.domain.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

public class AuthDto {

    /** POST /api/auth/kakao 요청 바디 */
    @Getter
    public static class KakaoLoginRequest {

        @NotBlank(message = "카카오 인가 코드는 필수입니다")
        @JsonProperty("kakao_code")
        private String kakaoCode;
    }

    /** POST /api/auth/kakao 응답 바디 */
    @Getter
    @Builder
    public static class KakaoLoginResponse {

        @JsonProperty("access_token")
        private String accessToken;

        private String nickname;

        @JsonProperty("is_new_user")
        private boolean isNewUser;
    }
}