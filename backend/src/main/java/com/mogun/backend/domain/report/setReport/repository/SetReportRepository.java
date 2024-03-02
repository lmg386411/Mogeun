package com.mogun.backend.domain.report.setReport.repository;

import com.mogun.backend.domain.report.routineReport.RoutineReport;
import com.mogun.backend.domain.report.setReport.SetReport;
import com.mogun.backend.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SetReportRepository extends JpaRepository<SetReport, Integer> {

    @Query(value = "SELECT * FROM set_report sr WHERE sr.routine_report_key = :rrk ORDER BY start_time", nativeQuery = true)
    List<SetReport> findAllByRoutineReport(@Param("rrk") RoutineReport report);

    @Query(value = "SELECT * FROM set_report sr WHERE sr.user_key = :user_key AND sr.start_time BETWEEN :before AND NOW() AND sr.set_number = 1", nativeQuery = true)
    List<SetReport> findRangedByUserAndStartDate(@Param("user_key")User user, @Param("before")LocalDateTime startDate);

    @Query(value = "SELECT sr.exec_key, COUNT(*) AS exec_count FROM set_report sr WHERE sr.user_key = :user_key AND sr.start_time BETWEEN :before AND NOW() AND sr.set_number = 1 GROUP BY sr.exec_key ORDER BY exec_count DESC", nativeQuery = true)
    List<ExecCountInterface> findRangedExerciseByUserAndStartDate(@Param("user_key")User user, @Param("before")LocalDateTime startDate);

    @Query(value = "SELECT sr.exec_key, sr.train_weight FROM set_report sr WHERE sr.user_key = :user_key AND sr.start_time BETWEEN :before AND NOW() AND sr.set_number = 1 ORDER BY train_weight DESC LIMIT 1", nativeQuery = true)
    List<ExecWeightInterface> findRangedWeightByUserAndStartDate(@Param("user_key")User user, @Param("before")LocalDateTime startDate);

    @Query(value = "SELECT sr.exec_key, sr.set_number FROM set_report sr WHERE sr.user_key = :user_key AND sr.start_time BETWEEN :before AND NOW() ORDER BY sr.set_number DESC LIMIT 1", nativeQuery = true)
    List<ExecSetInterface> findRangedSetByUserAndStartDate(@Param("user_key")User user, @Param("before")LocalDateTime startDate);

}
