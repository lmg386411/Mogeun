package com.mogun.backend.controller.report.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class CommonReportRequest {

    @JsonProperty("user_key")
    private int userKey;

    @JsonProperty("email")
    private String email;

    @JsonProperty("routine_key")
    private int routineKey;

    @JsonProperty("report_key")
    private Long reportKey;

    @JsonProperty("plan_key")
    private int planKey;

    @JsonProperty("is_attached")
    private char isAttached;

    @JsonProperty("start_time")
    private LocalDateTime startTime;

    @JsonProperty("end_time")
    private LocalDateTime endTime;

    @JsonProperty("routine_report_key")
    private Long routineReportKey;

    @JsonProperty("set_number")
    private int setNumber;

    @JsonProperty("weight")
    private int weight;

    @JsonProperty("target_rep")
    private int targetRepeat;

    @JsonProperty("success_rep")
    private int successRepeat;

    @JsonProperty("muscle_acts")
    private List<MuscleActReportRequest> actList;
}
