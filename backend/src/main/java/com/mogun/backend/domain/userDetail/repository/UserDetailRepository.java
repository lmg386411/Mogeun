package com.mogun.backend.domain.userDetail.repository;

import com.mogun.backend.domain.userDetail.UserDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDetailRepository extends JpaRepository<UserDetail, Integer> {

    @Query(value = "SELECT * FROM user_detail ud WHERE ud.user_key = :uk", nativeQuery = true)
    Optional<UserDetail> findByUserKey(@Param("uk") int userKey);
}
