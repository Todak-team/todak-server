package com.todak.todak.domain.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, unique = true, length = 100)
    private String kakaoId;

    @Column(nullable = false, length = 50)
    private String nickname;

    @Column(length = 200)
    private String email;

    @Column(length = 500)
    private String profileImageUrl;

    @Column(nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean isPro = false;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Builder
    public User(String kakaoId, String nickname, String email, String profileImageUrl) {
        this.kakaoId = kakaoId;
        this.nickname = nickname;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
        this.isPro = false;
        this.createdAt = LocalDateTime.now();
    }
}