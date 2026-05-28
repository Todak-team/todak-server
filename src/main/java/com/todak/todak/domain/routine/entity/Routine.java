package com.todak.todak.domain.routine.entity;

import com.todak.todak.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "routine")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Routine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long routineId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 200)
    private String planATitle;

    @Column(length = 200)
    private String planBTitle;

    @Column(nullable = false, columnDefinition = "INT DEFAULT 1")
    private Integer targetCount = 1;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Builder
    public Routine(User user, String planATitle, String planBTitle, Integer targetCount) {
        this.user = user;
        this.planATitle = planATitle;
        this.planBTitle = planBTitle;
        this.targetCount = targetCount != null ? targetCount : 1;
        this.createdAt = LocalDateTime.now();
    }

    public void update(String planATitle, String planBTitle, Integer targetCount) {
        if (planATitle != null) this.planATitle = planATitle;
        if (planBTitle != null) this.planBTitle = planBTitle;
        if (targetCount != null) this.targetCount = targetCount;
    }
}