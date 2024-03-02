package com.mogun.backend.service.report.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mogun.backend.domain.report.muscleActInSet.MuscleActInSetLog;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class SetResultDto {

    @JsonProperty("set_number")
    private int setNumber;

    @JsonProperty("weight")
    private float weight;

    @JsonProperty("target_rep")
    private int targetRep;

    @JsonProperty("success_rep")
    private int successRep;

    @JsonProperty("muscle_activity")
    private List<Double> muscleActivityList;

    @JsonProperty("muscle_fatigue")
    private List<Double> muscleFatigueList;
}
