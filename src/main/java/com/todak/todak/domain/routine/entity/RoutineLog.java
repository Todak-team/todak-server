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
@Table(name = "routine_log", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"routine_id", "log_date"})
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoutineLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Long logId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "routine_id", nullable = false)
    private Routine routine;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "log_date", nullable = false)
    private LocalDate logDate;

    @Column(name = "is_completed", nullable = false)
    private boolean completed;

    @Column(name = "completed_plan", columnDefinition = "char(1)")
    private String completedPlan;

    @Column(name = "emotion_score", nullable = false)
    private int emotionScore;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Builder
    public RoutineLog(Routine routine, User user, LocalDate logDate,
                      boolean completed, String completedPlan, int emotionScore) {
        this.routine = routine;
        this.user = user;
        this.logDate = logDate;
        this.completed = completed;
        this.completedPlan = completedPlan;
        this.emotionScore = emotionScore;
        this.createdAt = LocalDateTime.now();
    }
}
