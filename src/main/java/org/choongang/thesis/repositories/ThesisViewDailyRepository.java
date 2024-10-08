package org.choongang.thesis.repositories;

import org.choongang.thesis.entities.ThesisViewDaily;
import org.choongang.thesis.entities.ThesisViewDailyId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface ThesisViewDailyRepository extends JpaRepository<ThesisViewDaily, ThesisViewDailyId>, QuerydslPredicateExecutor<ThesisViewDaily> {
    void deleteByTid(Long tid);
}
