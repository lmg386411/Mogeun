package com.mogun.backend.service.report.dto;

import com.mogun.backend.domain.musclePart.MusclePart;
import com.mogun.backend.domain.report.routineReport.RoutineReport;
import com.mogun.backend.domain.report.routineResult.RoutineResult;
import com.mogun.backend.domain.report.usedMusclePart.UsedMusclePart;
import com.mogun.backend.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
public class ResultDto {

    private int userKey;
    private String userEmail;
    private int resultKey;
    private int muscleKey;
    private float consumeCalorie;
    private RoutineReport report;
    private Long reportKey;
    private LocalDate date;

    public RoutineResult toRoutineResultEntity(RoutineReport report) {
        return RoutineResult.builder()
                .routineReport(report)
                .user(report.getUser())
                .consumeCalorie(consumeCalorie)
                .routineDate(report.getStartTime().toLocalDate())
                .build();
    }

    public UsedMusclePart toUsedMusclePartEntity(RoutineResult result, MusclePart musclePart) {
        return UsedMusclePart.builder()
                .resultKey(result.getResultKey())
                .musclePart(musclePart)
                .build();
    }
}
