package com.mogun.backend.service.report.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mogun.backend.domain.report.routineReport.RoutineReport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class ResultListDto {

    private LocalDate date;

    @JsonProperty("routine_count")
    private int routineCount;

    @JsonProperty("routine_reports")
    private List<SimpleReportInfo> routineReports;
}
