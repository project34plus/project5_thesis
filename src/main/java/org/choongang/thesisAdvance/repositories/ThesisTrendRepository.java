package org.choongang.thesisAdvance.repositories;

import org.choongang.thesis.entities.Thesis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface ThesisTrendRepository extends JpaRepository<Thesis, Long>, QuerydslPredicateExecutor<Thesis> {
    //List<Thesis> findByDateBetween(LocalDate startDate, LocalDate endDate);

    //Page<Thesis> findAll(BooleanBuilder andBuilder, Pageable pageable);
}
