package org.choongang.thesis.repositories;

import org.choongang.thesis.entities.Thesis;
import org.choongang.thesis.entities.VersionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface VersionLogRepository extends JpaRepository<VersionLog, Long> , QuerydslPredicateExecutor<VersionLog> {
    List<VersionLog> findByThesis_Tid(Long tid);
    void deleteByThesis(Thesis thesis);
}
