package org.choongang.thesis.repositories;

import org.choongang.thesis.entities.VisitHistory;
import org.choongang.thesis.entities.VisitHistoryId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface VisitHistoryRepository extends JpaRepository<VisitHistory, VisitHistoryId>, QuerydslPredicateExecutor<VisitHistory> {
}
