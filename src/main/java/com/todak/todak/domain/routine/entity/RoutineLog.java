package com.todak.todak.domain.routine.entity;

import com.todak.todak.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "routine_log")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoutineLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "routine_id", nullable = false)
    private Routine routine;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDate logDate;

    @Column(nullable = false)
    private Boolean isCompleted;

    @Column(length = 1)
    private String completedPlan;

    private Integer emotionScore;

    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer currentCount = 0;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Builder
    public RoutineLog(Routine routine, User user, LocalDate logDate,
                      Boolean isCompleted, String completedPlan, Integer emotionScore, Integer currentCount) {
        this.routine = routine;
        this.user = user;
        this.logDate = logDate;
        this.isCompleted = isCompleted;
        this.completedPlan = completedPlan;
        this.emotionScore = emotionScore;
        this.currentCount = currentCount != null ? currentCount : 0;
        this.createdAt = LocalDateTime.now();
    }

    public void applyProgress(int delta, int targetCount) {
        this.currentCount = Math.max(0, (this.currentCount != null ? this.currentCount : 0) + delta);
        if (this.currentCount >= targetCount) {
            this.isCompleted = true;
        }
    }
}