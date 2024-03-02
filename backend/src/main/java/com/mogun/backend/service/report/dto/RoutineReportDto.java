package com.mogun.backend.service.report.dto;

import com.mogun.backend.domain.exercise.Exercise;
import com.mogun.backend.domain.report.routineReport.RoutineReport;
import com.mogun.backend.domain.report.setReport.SetReport;
import com.mogun.backend.domain.routine.userRoutine.UserRoutine;
import com.mogun.backend.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class RoutineReportDto {

    private String email;
    private int userKey;
    private int routineKey;

    private int planKey;
    private Long reportKey;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private char isAttached;

    private int setNumber;
    private int weight;
    private int targetRep;
    private int successRep;
    private List<MuscleActsDto> actsDtoList;

    public RoutineReport toRoutineReportEntity(User user, UserRoutine routine) {

        return RoutineReport.builder()
                .user(user)
                .routineName(routine.getRoutineName())
                .startTime(startTime)
                .isAttached(isAttached)
                .build();
    }

    public SetReport toSetReportEntity(RoutineReport report, Exercise exec) {

        return SetReport.builder()
                .routineReport(report)
                .user(report.getUser())
                .exercise(exec)
                .setNumber(setNumber)
                .trainWeight(weight)
                .targetRep(targetRep)
                .successesRep(successRep)
                .startTime(startTime)
                .endTime(endTime)
                .build();
    }
}
