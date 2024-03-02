package com.mogun.backend.controller.routine.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class AddPlanListRequest {

    @JsonProperty("routine_key")
    private int routineKey;

    @JsonProperty("exec_keys")
    private List<Integer> execKeys;
}
