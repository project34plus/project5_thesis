package org.choongang.thesisAdvance.repositories;

import org.choongang.thesis.entities.Thesis;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThesisTrendRepository extends JpaRepository<Thesis, String> {
    //List<Thesis> findTop10byDate(LocalDate startDate, LocalDate endDate);
}
