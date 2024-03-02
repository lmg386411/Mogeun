package com.mogun.backend.controller.routine.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class RoutineCreatedResponse {

    @JsonProperty("routine_key")
    private int routine_key;

    @JsonProperty("routine_name")
    private String routineName;
}
