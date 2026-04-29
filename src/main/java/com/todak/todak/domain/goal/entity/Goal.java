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
    @Column(name = "goal_id")
    private Long goalId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "plan_a_title", nullable = false)
    private String planATitle;

    @Column(name = "plan_b_title")
    private String planBTitle;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "is_completed", nullable = false)
    private boolean completed;

    @Column(name = "completed_plan", columnDefinition = "char(1)")
    private String completedPlan;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Builder
    public Goal(User user, String planATitle, String planBTitle, LocalDate dueDate) {
        this.user = user;
        this.planATitle = planATitle;
        this.planBTitle = planBTitle;
        this.dueDate = dueDate;
        this.completed = false;
        this.createdAt = LocalDateTime.now();
    }

    public void complete(String completedPlan) {
        this.completed = true;
        this.completedPlan = completedPlan;
    }
}
