package com.mogun.backend.service.routine.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
public class RoutineOutlineDto {

    @JsonProperty("routine_key")
    private int routineKey;

    @JsonProperty("name")
    private String name;

    @JsonProperty("image_path")
    private Set<String> muscleImagePathList;
}
