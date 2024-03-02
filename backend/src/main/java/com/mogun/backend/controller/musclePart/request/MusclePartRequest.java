package com.mogun.backend.controller.musclePart.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class MusclePartRequest {

    @JsonProperty("muscle_part_name")
    private String partName;

    @JsonProperty("image_path")
    private String imagePath;

    @JsonProperty("exec_key")
    private int execKey;

    @JsonProperty("partKey")
    private int partKey;

    @JsonProperty("attach_direction")
    private char direction;

    @JsonProperty("muscle_category")
    private char category;
}
