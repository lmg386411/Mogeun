package com.mogun.backend.domain.musclePart.repository;

import com.mogun.backend.domain.musclePart.MusclePart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MusclePartRepository extends JpaRepository<MusclePart, Integer> {
}
