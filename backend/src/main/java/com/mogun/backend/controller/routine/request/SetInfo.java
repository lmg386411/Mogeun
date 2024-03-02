package com.mogun.backend.controller.routine.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class SetInfo {

    @JsonProperty("set_number")
    private int setNumber;

    @JsonProperty("weight")
    private int weight;

    @JsonProperty("target_rep")
    private int targetRep;
}
