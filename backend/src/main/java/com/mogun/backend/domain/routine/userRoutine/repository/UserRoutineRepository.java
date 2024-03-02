package com.mogun.backend.domain.routine.userRoutine.repository;

import com.mogun.backend.domain.routine.userRoutine.UserRoutine;
import com.mogun.backend.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoutineRepository extends JpaRepository<UserRoutine, Integer> {

    @Query(value = "SELECT * FROM user_routine ur WHERE ur.user_key = :user_key ORDER BY ur.routine_key", nativeQuery = true)
    List<UserRoutine> findAllByUser(@Param("user_key") User user);
}
