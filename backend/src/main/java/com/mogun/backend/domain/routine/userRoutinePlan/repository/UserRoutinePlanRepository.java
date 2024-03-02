package com.mogun.backend.domain.routine.userRoutinePlan.repository;

import com.mogun.backend.domain.exercise.Exercise;
import com.mogun.backend.domain.routine.userRoutine.UserRoutine;
import com.mogun.backend.domain.routine.userRoutinePlan.UserRoutinePlan;
import com.mogun.backend.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRoutinePlanRepository extends JpaRepository<UserRoutinePlan, Integer> {

    List<UserRoutinePlan> findAllByUserRoutine(UserRoutine routine);

    @Query(value = "SELECT * FROM user_routine_plan urp WHERE urp.user_key = :user_key ORDER BY urp.routine_key", nativeQuery = true)
    List<UserRoutinePlan> findAllByUser(@Param("user_key") User user);
    Optional<UserRoutinePlan> findByUserRoutineAndExercise(UserRoutine routine, Exercise exec);

    void deleteAllByUserRoutine(UserRoutine routine);
}
