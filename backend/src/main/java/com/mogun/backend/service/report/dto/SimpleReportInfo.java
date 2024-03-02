package com.mogun.backend.service.report.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class SimpleReportInfo {

    @JsonProperty("routine_report_key")
    private Long reportKey;

    @JsonProperty("routine_name")
    private String routineName;

    @JsonProperty("start_time")
    private LocalDateTime startTime;

    @JsonProperty("end_time")
    private LocalDateTime endTime;
}
