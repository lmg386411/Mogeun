package com.mogun.backend.controller.routine.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class SimpleRoutineInfoResponse {

    @JsonProperty("routine_key")
    private int key;
    private String name;

    // Seongmin 이미지 추가
    @JsonProperty("image_path")
    private List<String> imagePath;
}
