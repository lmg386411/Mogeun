package com.mogun.backend.domain.userLog.userWeightLog.repository;

import com.mogun.backend.domain.userLog.userWeightLog.UserWeightLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserWeightLogRepository extends JpaRepository<UserWeightLog, Long> {
}
