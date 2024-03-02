package com.mogun.backend.domain.report.usedMusclePart.repository;

import com.mogun.backend.domain.report.usedMusclePart.UsedMusclePart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsedMusclePartRepository extends JpaRepository<UsedMusclePart, Integer> {
}
