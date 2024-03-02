package com.mogun.backend.controller.routine.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class SimpleMusclePartResponse {

    @JsonProperty("part_name")
    private String partName;

    @JsonProperty("image_path")
    private String imagePath;
}
