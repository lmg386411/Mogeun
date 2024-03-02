package com.mogun.backend.service.report.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class MostSetsDto {

    @JsonProperty("exec_name")
    private String execName;

    @JsonProperty("image_path")
    private String imagePath;

    @JsonProperty("set_count")
    private int setCount;
}
