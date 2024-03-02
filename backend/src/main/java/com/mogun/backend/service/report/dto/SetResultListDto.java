package com.mogun.backend.service.report.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mogun.backend.domain.exercise.Exercise;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class SetResultListDto {

    private int status;
    private Exercise exec;
    private List<SetResultDto> setResultDtoList;
}
