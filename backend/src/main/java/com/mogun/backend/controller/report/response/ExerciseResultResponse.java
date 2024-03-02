package com.mogun.backend.controller.report.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mogun.backend.service.report.dto.SetResultDto;
import com.mogun.backend.service.report.dto.SetResultListDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class ExerciseResultResponse {

    @JsonProperty("exec_name")
    private String execName;

    @JsonProperty("image_path")
    private String imagePath;

    @JsonProperty("set_result")
    List<SetResultDto> setResultList;
}
