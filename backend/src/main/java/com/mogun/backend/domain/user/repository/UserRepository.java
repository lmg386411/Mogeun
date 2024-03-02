package com.mogun.backend.domain.user.repository;

import com.mogun.backend.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);
    User findByPassword(String password);
    Optional<User> findByEmailAndPassword(String email, String password);

    boolean existsByEmail(String email);
    boolean existsByEmailAndPassword(String email, String password);

}
