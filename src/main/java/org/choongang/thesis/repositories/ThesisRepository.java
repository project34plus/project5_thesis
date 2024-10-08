package org.choongang.thesis.repositories;

import org.choongang.thesis.entities.Thesis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface ThesisRepository extends JpaRepository<Thesis, Long> , QuerydslPredicateExecutor<Thesis> {
    List<Thesis> findByTidInAndDeletedAtIsNull(List<Long> tids);
}
