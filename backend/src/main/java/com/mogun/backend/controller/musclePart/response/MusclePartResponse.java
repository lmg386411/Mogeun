package com.mogun.backend.controller.musclePart.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
public class MusclePartResponse {

    @JsonProperty("muscle_part_key")
    private int partKey;

    @JsonProperty("part_name")
    private String partName;

    @JsonProperty("image_path")
    private String imagePath;
}
