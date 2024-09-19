package org.choongang.thesis.trend;

import com.netflix.discovery.converters.Auto;
import org.choongang.global.tests.MockMember;
import org.choongang.member.MemberUtil;
import org.choongang.member.constants.Authority;
import org.choongang.member.entities.Member;
import org.choongang.thesis.entities.WishList;
import org.choongang.thesis.repositories.WishListRepository;
import org.choongang.thesisAdvance.controllers.TrendSearch;
import org.choongang.thesisAdvance.services.TrendInfoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
public class RankingTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberUtil memberUtil;

    @Autowired
    private WishListRepository wishListRepository;

    @Auto
    private TrendInfoService trendInfoService;

    @Test
    @MockMember // 기본적인 회원 정보 들어감
    @DisplayName("MockMember 테스트")
    void test1() {
        Member member = memberUtil.getMember();
        System.out.println(member);
    }

    @Test
    @MockMember(authority = Authority.ADMIN)
        //MockMember 없이 접근 -> 401 (회원 전용일 때)
    void test2() throws Exception {
        mockMvc.perform(get("/trend/field")).andDo(print());
    }

    @Test
    @MockMember
    void test3() {
        WishList wishList = new WishList();
        wishList.setTid(1L);
        wishListRepository.saveAndFlush(wishList);
    }

    @Test
    @DisplayName("키워드 조회")
    void test4() throws Exception{
        TrendSearch trendSearch = new TrendSearch();
        trendSearch.setSDate(LocalDate.of(2024, 1, 1));
        trendSearch.setEDate(LocalDate.now());
        trendSearch.setJob(Arrays.asList("직업1", "직업2"));
        System.out.println(trendInfoService.getKeywordRankingByJob(trendSearch));
    }

}
