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

    @Column(nullable = false)
    private Integer emotionScore;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Builder
    public RoutineLog(Routine routine, User user, LocalDate logDate,
                      Boolean isCompleted, String completedPlan, Integer emotionScore) {
        this.routine = routine;
        this.user = user;
        this.logDate = logDate;
        this.isCompleted = isCompleted;
        this.completedPlan = completedPlan;
        this.emotionScore = emotionScore;
        this.createdAt = LocalDateTime.now();
    }
}