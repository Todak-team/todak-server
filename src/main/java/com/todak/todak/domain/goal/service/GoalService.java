package com.todak.todak.domain.goal.service;

import com.todak.todak.domain.emotion.entity.DailyEmotion;
import com.todak.todak.domain.emotion.repository.DailyEmotionRepository;
import com.todak.todak.domain.goal.dto.GoalDto;
import com.todak.todak.domain.goal.entity.Goal;
import com.todak.todak.domain.goal.repository.GoalRepository;
import com.todak.todak.domain.user.entity.User;
import com.todak.todak.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GoalService {

    private final GoalRepository goalRepository;
    private final DailyEmotionRepository dailyEmotionRepository;
    private final UserRepository userRepository;

    @Transactional
    public GoalDto.CreateResponse create(Long userId, GoalDto.CreateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다"));

        Goal goal = Goal.builder()
                .user(user)
                .planATitle(request.getPlanATitle())
                .planBTitle(request.getPlanBTitle())
                .dueDate(request.getDueDate())
                .build();

        Goal saved = goalRepository.save(goal);

        return GoalDto.CreateResponse.builder()
                .goalId(saved.getGoalId())
                .planATitle(saved.getPlanATitle())
                .planBTitle(saved.getPlanBTitle())
                .dueDate(saved.getDueDate())
                .createdAt(saved.getCreatedAt())
                .build();
    }

    @Transactional(readOnly = true)
    public GoalDto.ListResponse getList(Long userId) {
        Integer emotionScore = dailyEmotionRepository
                .findByUserUserIdAndCheckedDate(userId, LocalDate.now())
                .map(DailyEmotion::getEmotionScore)
                .orElse(null);

        List<Goal> goals = goalRepository.findByUserUserIdAndIsCompletedFalseOrderByDueDateAsc(userId);

        List<GoalDto.GoalItem> items = goals.stream().map(goal ->
                GoalDto.GoalItem.builder()
                        .goalId(goal.getGoalId())
                        .planATitle(goal.getPlanATitle())
                        .planBTitle(goal.getPlanBTitle())
                        .dueDate(goal.getDueDate())
                        .isCompleted(goal.getIsCompleted())
                        .completedPlan(goal.getCompletedPlan())
                        .build()
        ).collect(Collectors.toList());

        return GoalDto.ListResponse.builder()
                .emotionScore(emotionScore)
                .goals(items)
                .build();
    }

    @Transactional
    public GoalDto.CompleteResponse complete(Long userId, Long goalId, GoalDto.CompleteRequest request) {
        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 목표를 찾을 수 없습니다"));

        if (!goal.getUser().getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "접근 권한이 없습니다");
        }

        goal.complete(request.getCompletedPlan());

        return GoalDto.CompleteResponse.builder()
                .goalId(goal.getGoalId())
                .isCompleted(true)
                .completedPlan(goal.getCompletedPlan())
                .build();
    }

    @Transactional
    public GoalDto.UpdateResponse update(Long userId, Long goalId, GoalDto.UpdateRequest request) {
        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 목표를 찾을 수 없습니다"));

        if (!goal.getUser().getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "접근 권한이 없습니다");
        }

        goal.update(request.getPlanATitle(), request.getPlanBTitle(), request.getDueDate());

        return GoalDto.UpdateResponse.builder()
                .goalId(goal.getGoalId())
                .planATitle(goal.getPlanATitle())
                .planBTitle(goal.getPlanBTitle())
                .dueDate(goal.getDueDate())
                .build();
    }

    @Transactional
    public void delete(Long userId, Long goalId) {
        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 목표를 찾을 수 없습니다"));

        if (!goal.getUser().getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "접근 권한이 없습니다");
        }

        goalRepository.delete(goal);
    }
}