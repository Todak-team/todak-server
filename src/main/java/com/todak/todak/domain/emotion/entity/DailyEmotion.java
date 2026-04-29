package com.todak.todak.domain.emotion.entity;

import com.todak.todak.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "daily_emotion", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "checked_date"})
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DailyEmotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "emotion_id")
    private Long emotionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "emotion_score", nullable = false)
    private int emotionScore;

    @Column(name = "checked_date", nullable = false)
    private LocalDate checkedDate;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Builder
    public DailyEmotion(User user, int emotionScore, LocalDate checkedDate) {
        this.user = user;
        this.emotionScore = emotionScore;
        this.checkedDate = checkedDate;
        this.updatedAt = LocalDateTime.now();
    }

    public void update(int emotionScore) {
        this.emotionScore = emotionScore;
        this.updatedAt = LocalDateTime.now();
    }
}
