package com.mogun.backend.service.report.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mogun.backend.domain.musclePart.MusclePart;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class ExerciseResultDto {

    @JsonProperty("exec_name")
    private String execName;

    @JsonProperty("image_path")
    private String imagePath;

    @JsonProperty("sets")
    private int sets;

    @JsonProperty("parts")
    private List<String> partList;

    @JsonProperty("muscle_image_paths")
    private List<String> muscleImagePathList;

    @JsonProperty("set_results")
    private List<SetResultDto> setResultList;
}
