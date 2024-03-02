package com.mogun.backend.controller.report.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class MuscleActReportRequest {

    @JsonProperty("sensor_number")
    private int sensorNumber;

    @JsonProperty("muscle_average")
    private double muscleAverage;

    @JsonProperty("muscle_fatigue")
    private double muscleFatigue;
}
