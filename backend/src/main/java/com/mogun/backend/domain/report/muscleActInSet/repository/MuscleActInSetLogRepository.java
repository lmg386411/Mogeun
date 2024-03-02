package com.mogun.backend.domain.report.muscleActInSet.repository;

import com.mogun.backend.domain.report.muscleActInSet.MuscleActInSetLog;
import com.mogun.backend.domain.report.setReport.SetReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MuscleActInSetLogRepository extends JpaRepository<MuscleActInSetLog, Long> {

    List<MuscleActInSetLog> findAllBySetReport(SetReport report);

    @Query(value = "SELECT act.muscle_activity FROM muscle_act_in_set_log act WHERE act.set_report_key = :report_key", nativeQuery = true)
    List<Double> findAllActivityBySetReport(@Param("report_key")SetReport report);

    @Query(value = "SELECT act.muscle_fatigue FROM muscle_act_in_set_log act WHERE act.set_report_key = :report_key", nativeQuery = true)
    List<Double> findAllFatigueBySetReport(@Param("report_key")SetReport report);
}
