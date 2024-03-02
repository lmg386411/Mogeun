package com.mogun.backend.controller.routine.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class PlanListResponse {

    @JsonProperty("routine_key")
    private int routineKey;

    @JsonProperty("routine_name")
    private String routineName;

    @JsonProperty("exercises")
    List<SimplePlanInfoResponse> exercises;
}
