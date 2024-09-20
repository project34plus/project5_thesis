package org.choongang.thesis.services;

import jakarta.persistence.EntityManager;
import org.choongang.global.ListData;
import org.choongang.global.exceptions.BadRequestException;
import org.choongang.global.rests.ApiRequest;
import org.choongang.global.tests.MockMember;
import org.choongang.member.MemberUtil;
import org.choongang.member.constants.Authority;
import org.choongang.member.entities.Member;
import org.choongang.thesis.entities.Thesis;
import org.choongang.thesisAdvance.controllers.RecommendSearch;
import org.choongang.thesisAdvance.services.RecommendInfoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class RecommendServiceTest {
    @Autowired
    private EntityManager em;
    @Autowired
    private RecommendInfoService recommendInfoService;
    @Autowired
    private MemberUtil memberUtil;
    @Autowired
    private ApiRequest apiRequest;

    @Test
    void test1() {
        String email = "test01@test.org";
        String sql = "SELECT * FROM THESIS_FIELDS tf WHERE THESES_TID IN (SELECT ul.tid FROM USER_LOG ul WHERE ul.email = :email GROUP BY ul.tid ) ORDER BY ( SELECT COUNT(ul.tid) FROM USER_LOG ul WHERE ul.tid = tf.THESES_TID) DESC";
        List<Object[]> result = em.createNativeQuery(sql)
                .setParameter("email", email)
                .getResultList();
        System.out.println("sql: " + sql);
        result.forEach(i -> {
            System.out.println(i[0]);
            System.out.println(i[1]);
        });
    }

    @Test
    @WithMockUser(username = "test01@test.org")
    @Transactional
    void test2() {
        RecommendSearch search = new RecommendSearch();
        ListData<Thesis> data = recommendInfoService.getList(SecurityContextHolder.getContext().getAuthentication().getName(), search);
        System.out.println(data.getItems().toString());
    }
    @Test
    @DisplayName("회원 정보 불러오기 테스트")
    @MockMember(email = "mock1@test.org", authority = Authority.ADMIN)
    void test3(){
        Member member = memberUtil.getMember();
        System.out.println(member);
        ApiRequest result = apiRequest.request("/admin/info/"+member.getEmail(),"member-service", HttpMethod.GET);
        if(!result.getStatus().is2xxSuccessful()){
            throw new BadRequestException("Fail.MemberInfo");
        }
        System.out.println(result.getData().toString());
    }
    @Test
    @MockMember(email="test@test.org")
    void test4(){
        ApiRequest result = apiRequest.request("/recommend/list","thesis-service");
        if(!result.getStatus().is2xxSuccessful()){
            throw new BadRequestException("잘못된 요청");
        }
        System.out.println(result.getData().toString());
    }
}
