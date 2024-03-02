package com.mogun.backend.service.userLog.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class SimpleMuscleMassLog {

    @JsonProperty("changed_time")
    private LocalDateTime changedTime;

    @JsonProperty("muscle_mass")
    private float muscleMass;
}
