package com.mogun.backend.domain.userLog.userBodyFatLog.repository;

import com.mogun.backend.domain.user.User;
import com.mogun.backend.domain.userLog.userBodyFatLog.UserBodyFatLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserBodyFatLogRepository extends JpaRepository<UserBodyFatLog, Long> {

    @Query(value = "SELECT * FROM user_body_fat_log fl WHERE fl.user_key = :user_key ORDER BY fl.changed_time DESC LIMIT 10", nativeQuery = true)
    List<UserBodyFatLog> findLast10LogByUser(@Param("user_key") User user);
}
