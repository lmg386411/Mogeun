package com.mogun.backend.domain.attachPart.repository;

import com.mogun.backend.domain.attachPart.AttachPart;
import com.mogun.backend.domain.exercise.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AttachPartRepository extends JpaRepository<AttachPart, Integer> {

    List<AttachPart> findAllByExercise(Exercise exercise);

    @Query(value = "SELECT * FROM attach_part ap WHERE ap.exec_key = :exec_key AND ap.attach_direction = 'L' AND ap.muscle_category = 'M'", nativeQuery = true)
    Optional<AttachPart> findMainPartByExercise(@Param("exec_key") Exercise exec);

    // Seongmin 부착 부위 왼쪽만 가져오기
    List<AttachPart> findAllByExerciseAndAttachDirection(Exercise exercise, char L);
}
