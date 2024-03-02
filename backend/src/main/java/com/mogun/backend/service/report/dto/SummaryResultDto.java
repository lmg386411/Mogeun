package com.mogun.backend.service.report.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mogun.backend.domain.exercise.Exercise;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class SummaryResultDto {

    @JsonProperty("name")
    private String routineName;

    @JsonProperty("date")
    private LocalDate routineDate;

    @JsonProperty("calorie")
    private float consumeCalorie;

    @JsonProperty("perform_time")
    private Long performTime;

    @JsonProperty("total_sets")
    private int totalSets;

    @JsonProperty("exercises")
    List<ExerciseResultDto> exerciseResultDtoList;
}
