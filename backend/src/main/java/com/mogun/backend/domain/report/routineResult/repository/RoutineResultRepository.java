package com.mogun.backend.domain.report.routineResult.repository;

import com.mogun.backend.domain.report.routineReport.RoutineReport;
import com.mogun.backend.domain.report.routineResult.RoutineResult;
import com.mogun.backend.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoutineResultRepository extends JpaRepository<RoutineResult, Integer> {

    Optional<RoutineResult> findByRoutineReport(RoutineReport report);

    @Query(value = "SELECT * FROM routine_result rr WHERE rr.routine_date >= :before AND rr.user_key = :user ORDER BY rr.routine_date", nativeQuery = true)
    List<RoutineResult> findAllByFromRoutineDateAndUser(@Param("before") LocalDate before, @Param("user") User user);

    @Query(value = "SELECT * FROM routine_result rr WHERE rr.routine_date >= :before AND rr.routine_date <= :after AND rr.user_key = :user ORDER BY rr.routine_date", nativeQuery = true)
    List<RoutineResult> findAllByBetweenRoutineDateAndUser(@Param("before") LocalDate before, @Param("after") LocalDate after, @Param("user") User user);
}
