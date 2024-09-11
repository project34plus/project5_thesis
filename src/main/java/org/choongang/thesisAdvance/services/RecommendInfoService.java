package org.choongang.thesisAdvance.services;

import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import org.choongang.global.ListData;
import org.choongang.member.entities.Member;
import org.choongang.thesis.entities.QThesis;
import org.choongang.thesis.entities.Thesis;
import org.choongang.thesisAdvance.controllers.RecommendSearch;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendInfoService {
    /**
     * 추천 논문 목록
     *
     * @return
     */
    public ListData<Thesis> getList(Member member, RecommendSearch search) {
        int page = Math.max(search.getPage(), 1);
        int limit = search.getLimit();
        limit = limit < 1 ? 20 : limit;
        /*회원별 카테고리, 관심분야 처리 S*/
//        search.setCategory();

        /*회원별 카테고리, 관심분야 처리 E*/
        /* 검색 처리 S */
        BooleanBuilder andBuilder = new BooleanBuilder();
        QThesis thesis = QThesis.thesis;
        String sopt = search.getSopt();
        String skey = search.getSkey();
        List<String> category = search.getCategory();
        List<String> fields = search.getFields();
        List<String> email = search.getEmail();
        return null;
    }
}
