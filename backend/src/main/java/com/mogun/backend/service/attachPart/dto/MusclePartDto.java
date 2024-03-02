package com.mogun.backend.service.attachPart.dto;

import com.mogun.backend.domain.exercise.Exercise;
import com.mogun.backend.domain.musclePart.MusclePart;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class MusclePartDto {

    private int partKey;
    private MusclePart part;
    private String partName;
    private String imagePath;

    private int execKey;
    private Exercise exercise;
    private char direction;
    private char category;

    public MusclePart toMusclePartEntity() {

        return MusclePart.builder()
                .partName(this.partName)
                .imagePath(this.imagePath)
                .build();
    }
}
