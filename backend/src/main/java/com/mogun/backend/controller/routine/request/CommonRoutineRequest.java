package com.mogun.backend.controller.routine.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CommonRoutineRequest {

    @JsonProperty("user_key")
    private int userKey;

    @JsonProperty("user_email")
    private String email;

    @JsonProperty("routine_name")
    private String routineName;

    @JsonProperty("routine_key")
    private int routineKey;

    @JsonProperty("exec_key")
    private int execKey;

    @JsonProperty("total_sets")
    private int sets;

    @JsonProperty("plan_key")
    private int planKey;

    @JsonProperty("set_key")
    private int setKey;
}
