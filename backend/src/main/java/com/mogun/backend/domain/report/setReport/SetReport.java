package com.mogun.backend.domain.report.setReport;

import com.mogun.backend.domain.exercise.Exercise;
import com.mogun.backend.domain.report.routineReport.RoutineReport;
import com.mogun.backend.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SetReport {

    @Id
    @GeneratedValue
    @Column(name = "set_report_key")
    private Long setReportKey;

    @ManyToOne
    @JoinColumn(name = "routine_report_key")
    private RoutineReport routineReport;

    @ManyToOne
    @JoinColumn(name = "user_key")
    private User user;

    @ManyToOne
    @JoinColumn(name = "exec_key")
    private Exercise exercise;

    @Column(name = "set_number")
    private int setNumber;

    @Column(name = "train_weight")
    private float trainWeight;

    @Column(name = "target_rep")
    private int targetRep;

    @Column(name = "success_rep")
    private int successesRep;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

}
