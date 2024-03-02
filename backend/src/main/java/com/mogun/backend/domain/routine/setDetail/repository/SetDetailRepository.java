package com.mogun.backend.domain.routine.setDetail.repository;

import com.mogun.backend.domain.routine.setDetail.SetDetail;
import com.mogun.backend.domain.routine.userRoutine.UserRoutine;
import com.mogun.backend.domain.routine.userRoutinePlan.UserRoutinePlan;
import com.mogun.backend.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SetDetailRepository extends JpaRepository<SetDetail, Integer> {

    List<SetDetail> findAllByUserRoutinePlan(UserRoutinePlan plan);
    void deleteAllByUserRoutinePlan(UserRoutinePlan plan);
}
