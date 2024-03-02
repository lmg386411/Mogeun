package com.mogun.backend.controller.report.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CommonResultRequest {

    @JsonProperty("routine_report_key")
    private Long reportKey;

    @JsonProperty("consume_calorie")
    private float consumeCalorie;
}
