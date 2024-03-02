package com.mogun.backend.controller.routine.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class SimplePlanInfoResponse {

    @JsonProperty("plan_key")
    private int planKey;

    @JsonProperty("exec_key")
    private int execKey;

    @JsonProperty("name")
    private String execName;

    @JsonProperty("eng_name")
    private String engName;

    @JsonProperty("sensing_part")
    private List<String> musclePart;

    @JsonProperty("main_part")
    private SimpleMusclePartResponse mainPart;

    @JsonProperty("image_path")
    private String imagePath;
}
