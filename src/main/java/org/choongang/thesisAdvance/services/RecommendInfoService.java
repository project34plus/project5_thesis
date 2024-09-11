package org.choongang.thesisAdvance.services;

import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import org.choongang.global.ListData;
import org.choongang.member.entities.Member;
import org.choongang.thesis.entities.QThesis;
import org.choongang.thesis.entities.Thesis;
import org.choongang.thesis.repositories.FieldRepository;
import org.choongang.thesis.repositories.InterestsRepository;
import org.choongang.thesisAdvance.controllers.RecommendSearch;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendInfoService {
    private final InterestsRepository interestsRepository;
    private final FieldRepository fieldRepository;

    /**
     * 추천 논문 목록
     *
     * @return
     */
    public ListData<Thesis> getList(Member member, RecommendSearch search) {
        int page = Math.max(search.getPage(), 1);
        int limit = search.getLimit();
        limit = limit < 1 ? 20 : limit;

        /* 검색 처리 S */
        BooleanBuilder andBuilder = new BooleanBuilder();
        QThesis thesis = QThesis.thesis;
        String sopt = search.getSopt();
        String skey = search.getSkey();

        /*회원별 카테고리, 관심분야 처리 S*/
        String email = member.getEmail();
        List<String> ids = interestsRepository.findIdsByEmail(email);
        fieldRepository.findByIdIn(ids).forEach(i -> andBuilder.or(thesis.fields.contains(i)));
        /*회원별 카테고리, 관심분야 처리 E*/

        


        return null;
    }
}
