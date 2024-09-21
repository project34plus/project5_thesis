package org.choongang.thesisAdvance.services;

import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.choongang.file.services.FileInfoService;
import org.choongang.global.ListData;
import org.choongang.global.Utils;
import org.choongang.global.exceptions.BadRequestException;
import org.choongang.global.rests.ApiRequest;
import org.choongang.member.entities.Member;
import org.choongang.thesis.entities.Field;
import org.choongang.thesis.entities.Thesis;
import org.choongang.thesis.exceptions.FieldNotFoundException;
import org.choongang.thesis.repositories.FieldRepository;
import org.choongang.thesis.repositories.InterestsRepository;
import org.choongang.thesis.repositories.ThesisRepository;
import org.choongang.thesis.services.ThesisInfoService;
import org.choongang.thesisAdvance.controllers.RecommendSearch;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendInfoService {
    private final InterestsRepository interestsRepository;
    private final FieldRepository fieldRepository;
    private final HttpServletRequest request;
    private final EntityManager em;
    private final ThesisRepository thesisRepository;
    private final ApiRequest apiRequest;
    private final Utils utils;
    private final FileInfoService fileInfoService;
    private final ThesisInfoService thesisInfoService;

    /**
     * 추천 논문 목록
     *
     * @return
     */
    public ListData<Thesis> getList(String email, RecommendSearch search) {
        /*회원별 소속 분야, 관심 분야 처리 S*/
        //관심분야
        List<String> ids = interestsRepository.findIdsByEmail(email);
        /*회원별 카테고리, 관심분야 처리 E*/

        /*회원별 조회한 논문의 분야 불러오기*/
        getThesisFieldFromUserLog(email).forEach(i -> {
            Field field = fieldRepository.findById(i[1].toString()).orElseThrow(FieldNotFoundException::new);
            ids.add(field.getId());
        });
        //소속 분야
        try {
            ApiRequest result = apiRequest.request("/account", "member-service", HttpMethod.GET);
            if (!result.getStatus().is2xxSuccessful()) {
                throw new BadRequestException("회원 정보 불러오기 실패");
            }
            Member member = result.toObj(Member.class);
            if (member.getMemMajor() != null) {
                ids.add(member.getMemMajor());
            }
            if (member.getMemMinor() != null) {
                ids.add(member.getMemMinor());
            }
        } catch (BadRequestException e) {
            e.printStackTrace();
        }


        String sort = "viewCount_DESC";
        search.setSort(sort);

        search.setFields(ids);
        //찜한 리스트에서

        return thesisInfoService.getList(search);
    }

    private List<Object[]> getThesisFieldFromUserLog(String email) {
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
