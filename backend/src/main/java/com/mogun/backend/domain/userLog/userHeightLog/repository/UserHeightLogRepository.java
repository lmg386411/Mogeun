package com.mogun.backend.domain.userLog.userHeightLog.repository;

import com.mogun.backend.domain.userLog.userHeightLog.UserHeightLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserHeightLogRepository extends JpaRepository<UserHeightLog, Long> {
}
