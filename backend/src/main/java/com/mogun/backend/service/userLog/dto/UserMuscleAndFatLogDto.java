package com.mogun.backend.service.userLog.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class UserMuscleAndFatLogDto {

    private int userKey;

    @JsonProperty("body_fat_change_log")
    private List<SimpleBodyFatLog> bodyFatLogList;

    @JsonProperty("muscle_mass_change_log")
    private List<SimpleMuscleMassLog> muscleMassLogList;
}
