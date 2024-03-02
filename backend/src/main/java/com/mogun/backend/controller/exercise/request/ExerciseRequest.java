package com.mogun.backend.controller.exercise.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ExerciseRequest {

    @JsonProperty("name")
    private String execName;

    @JsonProperty("eng_name")
    private String engName;

    @JsonProperty("image_path")
    private String imagePath;

    @JsonProperty("main_part")
    private int partKey;
}
