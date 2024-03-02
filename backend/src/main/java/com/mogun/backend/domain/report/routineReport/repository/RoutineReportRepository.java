package com.mogun.backend.domain.report.routineReport.repository;

import com.mogun.backend.domain.report.routineReport.RoutineReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoutineReportRepository extends JpaRepository<RoutineReport, Long> {
}
