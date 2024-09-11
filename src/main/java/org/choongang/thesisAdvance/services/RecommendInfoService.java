package org.choongang.thesisAdvance.services;

import com.querydsl.core.BooleanBuilder;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.choongang.global.ListData;
import org.choongang.global.Pagination;
import org.choongang.thesis.entities.Field;
import org.choongang.thesis.entities.QThesis;
import org.choongang.thesis.entities.Thesis;
import org.choongang.thesis.exceptions.FieldNotFoundException;
import org.choongang.thesis.repositories.FieldRepository;
import org.choongang.thesis.repositories.InterestsRepository;
import org.choongang.thesis.repositories.ThesisRepository;
import org.choongang.thesisAdvance.controllers.RecommendSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.data.domain.Sort.Order.desc;

@Service
@RequiredArgsConstructor
public class RecommendInfoService {
    private final InterestsRepository interestsRepository;
    private final FieldRepository fieldRepository;
    private final HttpServletRequest request;
    private final EntityManager em;
    private final ThesisRepository thesisRepository;

    /**
     * 추천 논문 목록
     *
     * @return
     */
    public ListData<Thesis> getList(String email, RecommendSearch search) {
        int page = Math.max(search.getPage(), 1);
        int limit = search.getLimit();
        limit = limit < 1 ? 20 : limit;

        /* 검색 처리 S */
        BooleanBuilder andBuilder = new BooleanBuilder();
        QThesis thesis = QThesis.thesis;
        String sopt = search.getSopt();
        String skey = search.getSkey();

        /*회원별 카테고리, 관심분야 처리 S*/
        List<String> ids = interestsRepository.findIdsByEmail(email);
        fieldRepository.findByIdIn(ids).forEach(i -> andBuilder.or(thesis.fields.contains(i)));
        /*회원별 카테고리, 관심분야 처리 E*/

        /*회원별 조회한 논문의 분야 불러오기*/
        getThesisFieldLog(email).forEach(i -> {
            Field field = fieldRepository.findById(i[1].toString()).orElseThrow(FieldNotFoundException::new);
            andBuilder.or(thesis.fields.contains(field));
        });

        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(desc("createdAt")));
        Page<Thesis> data = thesisRepository.findAll(andBuilder, pageable);

        long total = data.getTotalElements();
        Pagination pagination = new Pagination(page, (int) total, 10, limit, request);

        List<Thesis> items = data.getContent();

        return new ListData<>(items, pagination);
    }

    private List<Object[]> getThesisFieldLog(String email) {
        String sql = "SELECT * FROM THESIS_FIELDS tf WHERE THESES_TID IN (SELECT ul.tid FROM USER_LOG ul WHERE ul.email = :email GROUP BY ul.tid ) ORDER BY ( SELECT COUNT(ul.tid) FROM USER_LOG ul WHERE ul.tid = tf.THESES_TID) DESC";
        return em.createNativeQuery(sql)
                .setParameter("email", email)
                .getResultList();
        /*result.forEach(i -> {
            System.out.println(i[0]);//2
            System.out.println(i[1]);//
        });*/
    }
}
