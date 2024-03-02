package com.mogun.backend.domain.report.usedMusclePart;

import com.mogun.backend.domain.musclePart.MusclePart;
import com.mogun.backend.domain.report.routineResult.RoutineResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsedMusclePart {

    @Id
    @Column(name = "result_key")
    private int resultKey;

    @ManyToOne
    @JoinColumn(name = "part_key")
    private MusclePart musclePart;

    @MapsId
    @ManyToOne
    @JoinColumn(name = "result_key")
    private RoutineResult routineResult;

}
