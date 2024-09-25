package org.choongang.thesis.repositories;

import org.choongang.thesis.entities.CommentData;
import org.choongang.thesis.entities.QCommentData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentDataRepository extends JpaRepository<CommentData, Long>, QuerydslPredicateExecutor<CommentData> {
    //논문별 댓글 갯수
    default int getTotal(Long tid) {
        QCommentData commentData = QCommentData.commentData;

        return (int) count(commentData.thesis.tid.eq(tid));
    }

    @Query("SELECT cd from CommentData cd where cd.thesis.tid = :tid order by cd.createdAt asc ")
    List<CommentData> findCommentDataByTid(@Param("tid") Long tid);
}
