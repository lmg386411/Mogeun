package com.mogun.backend.controller.routine.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class AddPlanListResponse {

    @JsonProperty("Added")
    private List<Integer> addedExec;

    @JsonProperty("Failed")
    private List<Integer> failedExec;
}
