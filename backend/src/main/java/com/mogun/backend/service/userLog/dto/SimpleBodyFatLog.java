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
public class SimpleBodyFatLog {

    @JsonProperty("changed_time")
    private LocalDateTime changedTime;

    @JsonProperty("body_fat")
    private float bodyFat;
}
