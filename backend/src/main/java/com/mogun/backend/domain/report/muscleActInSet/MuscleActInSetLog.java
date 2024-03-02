package com.mogun.backend.domain.report.muscleActInSet;

import com.mogun.backend.domain.musclePart.MusclePart;
import com.mogun.backend.domain.report.routineReport.RoutineReport;
import com.mogun.backend.domain.report.setReport.SetReport;
import com.mogun.backend.domain.user.User;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MuscleActInSetLog implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "set_act_key")
    private Long setLogKey;

    @ManyToOne
    @JoinColumn(name = "set_report_key")
    private SetReport setReport;

    @ManyToOne
    @JoinColumn(name = "part_key")
    private MusclePart musclePart;

    @Column(name = "sensor_number", columnDefinition = "TINYINT")
    private int sensorNumber;

    @Column(name = "muscle_activity")
    private double muscleActivity;

    @Column(name = "muscle_fatigue")
    private double muscleFatigue;

}
