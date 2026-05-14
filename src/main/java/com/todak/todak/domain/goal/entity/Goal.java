package com.todak.todak.domain.goal.entity;

import com.todak.todak.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "goal")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Goal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long goalId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 200)
    private String planATitle;

    @Column(length = 200)
    private String planBTitle;

    private LocalDate dueDate;

    @Column(nullable = false)
    private Boolean isCompleted;

    @Column(length = 1)
    private String completedPlan;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Builder
    public Goal(User user, String planATitle, String planBTitle, LocalDate dueDate) {
        this.user = user;
        this.planATitle = planATitle;
        this.planBTitle = planBTitle;
        this.dueDate = dueDate;
        this.isCompleted = false;
        this.createdAt = LocalDateTime.now();
    }

    public void complete(String completedPlan) {
        this.isCompleted = true;
        this.completedPlan = completedPlan;
    }
}