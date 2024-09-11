package org.choongang.thesis.services;

import org.choongang.global.ListData;
import org.choongang.thesis.controllers.ThesisSearch;
import org.choongang.thesis.entities.Thesis;
import org.choongang.thesis.repositories.ThesisRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

@SpringBootTest
@ActiveProfiles("dev")
public class TrendInfoTest {

    @Autowired
    ThesisInfoService infoService;

    @Autowired
    ThesisRepository thesisRepository;

    @Test
    @DisplayName("기간별 조회 기능 테스트")
    void test1() {
        LocalDate eDate = LocalDate.now().plusDays(1L);
        ThesisSearch search = new ThesisSearch();
        search.setEDate(eDate);
        ListData<Thesis> theses = infoService.getList(search);
        //theses.getItems().forEach(System.out::println);
        System.out.println("Theses count: " + theses.getItems().size());
        //theses.getItems().forEach(i ->System.out.println("Thesis: " + i));
    }
}
