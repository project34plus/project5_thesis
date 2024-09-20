package org.choongang.thesis.repositories;

import org.choongang.thesis.entities.CommentData;
import org.choongang.thesis.entities.QCommentData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface CommentDataRepository extends JpaRepository<CommentData, Long> , QuerydslPredicateExecutor<CommentData> {
    //논문별 댓글 갯수
    default int getTotal(Long tid) {
        QCommentData commentData = QCommentData.commentData;

        return (int)count(commentData.thesis.tid.eq(tid));
    }
}
