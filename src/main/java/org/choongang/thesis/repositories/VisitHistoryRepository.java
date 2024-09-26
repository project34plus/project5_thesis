package org.choongang.thesis.repositories;

import org.choongang.thesis.entities.VisitHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface VisitHistoryRepository extends JpaRepository<VisitHistory, Long>, QuerydslPredicateExecutor<VisitHistory> {
    List<VisitHistory> findByEmailOrderBySearchDateDesc(String email);
}
