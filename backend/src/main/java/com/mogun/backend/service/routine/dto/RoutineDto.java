package com.mogun.backend.service.routine.dto;

import com.mogun.backend.domain.exercise.Exercise;
import com.mogun.backend.domain.routine.setDetail.SetDetail;
import com.mogun.backend.domain.routine.userRoutine.UserRoutine;
import com.mogun.backend.domain.routine.userRoutinePlan.UserRoutinePlan;
import com.mogun.backend.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class RoutineDto {

    // 회원 정보
    private int userKey;

    // 루틴 정보
    private int routineKey;
    private String routineName;
    private int performCount;
    private char isDelete;

    // 단일 운동 계획 정보
    private int planKey;
    private int execKey;
    private Exercise exec;
    private int setAmount;

    // 계획 내 세트별 세부 사항
    private int setKey;
    private User user;
    private UserRoutine routine;
    private UserRoutinePlan plan;
    private int setNumber;
    private int weight;
    private int targetRep;

    public UserRoutine toRoutineEntity(User user) {
        return UserRoutine.builder()
                .user(user)
                .routineName(routineName)
                .count(0)
                .isDeleted('N')
                .build();
    }

    public UserRoutinePlan toRoutinePlanEntity(UserRoutine routine, Exercise exec) {
        return UserRoutinePlan.builder()
                .userRoutine(routine)
                .user(routine.getUser())
                .exercise(exec)
                .setAmount(setAmount)
                .build();
    }

    public SetDetail toSetDetailEntity(UserRoutinePlan plan) {
        return SetDetail.builder()
                .userRoutinePlan(plan)
                .setNumber(setNumber)
                .weight(weight)
                .targetRepeat(targetRep)
                .build();
    }
}
