package com.mogun.backend.domain.report.routineResult;

import com.mogun.backend.domain.report.routineReport.RoutineReport;
import com.mogun.backend.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoutineResult {

    @Id
    @GeneratedValue
    @Column(name = "result_key")
    private int resultKey;

    @ManyToOne
    @JoinColumn(name = "routine_report_key")
    private RoutineReport routineReport;

    @ManyToOne
    @JoinColumn(name = "user_key")
    private User user;

    @Column(name = "consume_calorie")
    private float consumeCalorie;

    @Column(name = "routine_date")
    private LocalDate routineDate;
}
