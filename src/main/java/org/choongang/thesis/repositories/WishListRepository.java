package org.choongang.thesis.repositories;

import org.choongang.thesis.entities.WishList;
import org.choongang.thesis.entities.WishListId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WishListRepository extends JpaRepository<WishList, WishListId>, QuerydslPredicateExecutor<WishList> {
    @Query("SELECT wl.tid from WishList wl where wl.email=:email")
    List<String> findWishListByEmail(@Param("email") String email);
    void deleteByTid(Long tid);
}
