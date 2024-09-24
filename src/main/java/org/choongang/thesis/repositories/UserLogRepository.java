package org.choongang.thesis.repositories;

import org.choongang.thesis.entities.UserLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserLogRepository extends JpaRepository<UserLog, Long>, QuerydslPredicateExecutor<UserLog> {
    //List<UserLog> findByEmailInAndSearchDateBetween(List<String> emails, LocalDateTime startDate, LocalDateTime endDate);
    //List<UserLog> findByJobInSearchDateBetween(List<String> job, LocalDateTime startDate, LocalDateTime endDate);
    @Query("SELECT Distinct(ul.search) from UserLog ul where ul.email=:email")
    List<String> findDistinctSearchByEmail(@Param("email")String email);
}

