package com.mogun.backend.service.report.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class MuscleActsDto {

    private int sensorNumber;
    private double muscleAverage;
    private double muscleFatigue;
}
