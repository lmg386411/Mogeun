package com.mogun.backend.domain.userLog.userMuscleMassLog.repository;

import com.mogun.backend.domain.user.User;
import com.mogun.backend.domain.userLog.userMuscleMassLog.UserMuscleMassLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMuscleMassLogRepository extends JpaRepository<UserMuscleMassLog, Long> {

    @Query(value = "SELECT * FROM user_muscle_mass_log ml WHERE ml.user_key = :user_key ORDER BY ml.changed_time DESC LIMIT 10", nativeQuery = true)
    List<UserMuscleMassLog> findLast10LogByUser(@Param("user_key")User user);
}
