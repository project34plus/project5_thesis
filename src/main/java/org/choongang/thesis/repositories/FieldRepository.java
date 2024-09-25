package org.choongang.thesis.repositories;

import org.choongang.thesis.entities.Field;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Transactional
public interface FieldRepository extends JpaRepository<Field, String> , QuerydslPredicateExecutor<Field> {
    List<Field> findByIdIn(List<String> ids);
    @Query("SELECT DISTINCT(f.name) FROM Field f")
    List<String> findDistinctName();
    Field findBySubfield(String subfield); //테스트용
    List<Field> findByName(String name); //테스트용

    @Query("SELECT f.id FROM Field f WHERE f.name = :name")
    List<String> findIdByName(@Param("name") String name);
}
